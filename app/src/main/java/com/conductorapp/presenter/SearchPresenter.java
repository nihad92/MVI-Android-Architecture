package com.conductorapp.presenter;

import com.conductorapp.presenter.base.AbstractPresenter;
import com.conductorapp.view.SearchView;
import com.conductorapp.viewstate.SearchViewState;
import conductor.com.data.repository.GitUserDataSource;
import io.reactivex.Observable;
import javax.inject.Inject;


public class SearchPresenter extends AbstractPresenter<SearchView, SearchViewState> {
  private final GitUserDataSource gitUserDataSource;
  @Inject public SearchPresenter(GitUserDataSource gitUserDataSource) {
    this.gitUserDataSource = gitUserDataSource;
  }

  @Override protected void bindIntents() {
    Observable<SearchViewState> observable = intent(SearchView::searchIntent)
        .switchMap(x -> gitUserDataSource.fetch(x.toString()))
        .map(x -> {
          if(!x.isEmpty()) {
            return new SearchViewState.SearchResult("", x);
          } else {
            return new SearchViewState.Empty("");
          }
        })
        .startWith(new SearchViewState.Loading(true))
        .onErrorReturn(throwable -> new SearchViewState.Error("", throwable));

    subscribeViewState(observable, SearchView::render);
  }
}
