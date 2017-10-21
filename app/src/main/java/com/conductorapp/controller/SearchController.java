package com.conductorapp.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import butterknife.BindView;
import com.conductorapp.AndroidApplication;
import com.conductorapp.R;
import com.conductorapp.adapter.UsersAdapter;
import com.conductorapp.controller.base.BaseController;
import com.conductorapp.di.components.DaggerControllerBaseComponent;
import com.conductorapp.presenter.SearchPresenter;
import com.conductorapp.presenter.base.AbstractPresenter;
import com.conductorapp.presenter.base.Presenter;
import com.conductorapp.viewstate.SearchViewState;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class SearchController extends BaseController implements com.conductorapp.view.SearchView {
  @BindView(R.id.search_view) SearchView searchView;
  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  @Inject SearchPresenter searchPresenter;
  @Inject UsersAdapter usersAdapter;

  private Snackbar loader;

  @Override protected void onContextAvailable(@NonNull Context context) {
    super.onContextAvailable(context);
    DaggerControllerBaseComponent.builder()
        .appComponent(((AndroidApplication) getActivity().getApplication()).getAppComponent())
        .build()
        .inject(this);
  }

  @Override public int getLayoutResId() {
    return R.layout.home;
  }

  @Override public AbstractPresenter getPresenter() {
    return searchPresenter;
  }

  @Override public void setPresenter(Presenter presenter) {
    searchPresenter = (SearchPresenter) presenter;
  }

  @Override public String getPresenterTag() {
    return "SEARCH";
  }

  @Override protected void onAttach(@NonNull View view) {
    super.onAttach(view);
    recyclerView.setAdapter(usersAdapter);
  }

  @Override public void render(SearchViewState viewState) {
    if(viewState instanceof SearchViewState.Error) renderView((SearchViewState.Error) viewState);
    if(viewState instanceof SearchViewState.Loading) renderView((SearchViewState.Loading) viewState);
    if(viewState instanceof SearchViewState.SearchResult) renderView((SearchViewState.SearchResult) viewState);
  }

  @Override public Observable<CharSequence> searchIntent() {
    return RxSearchView.queryTextChanges(searchView)
        .debounce(300, TimeUnit.MILLISECONDS);
  }

  private void renderView(SearchViewState.Error error) {

  }

  private void renderView(SearchViewState.SearchResult result) {
    if(loader != null)
      loader.dismiss();
    usersAdapter.setUsers(result.getGitUsers());
  }

  private void renderView(SearchViewState.Loading loading) {
    loader = Snackbar.make(getView(), "Loading...", Snackbar.LENGTH_INDEFINITE);
    loader.show();
  }
}
