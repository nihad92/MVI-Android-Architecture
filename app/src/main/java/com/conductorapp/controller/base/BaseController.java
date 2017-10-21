package com.conductorapp.controller.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bluelinelabs.conductor.Controller;
import com.conductorapp.presenter.base.Presenter;
import com.conductorapp.presenter.base.PresenterPool;
import com.conductorapp.view.base.BaseView;
import javax.inject.Inject;

public abstract class BaseController extends Controller implements BaseView{
  private Unbinder unbinder;
  @Inject PresenterPool presenterPool;

  @NonNull @Override
  protected final View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
    View view = inflater.inflate(getLayoutResId(), container, false);
    setRetainViewMode(RetainViewMode.RELEASE_DETACH);
    unbinder = ButterKnife.bind(this, view);
    if(presenterPool != null && presenterPool.hasPresenter(getPresenterTag())) {
      setPresenter(presenterPool.getPresenter(getPresenterTag()));
    } else {
      presenterPool.addPresenter(getPresenterTag(), getPresenter());
    }
    return view;
  }

  @Override protected void onAttach(@NonNull View view) {
    super.onAttach(view);
    if(getPresenter() != null) getPresenter().onAttach(this);
  }

  @Override protected void onDetach(@NonNull View view) {
    super.onDetach(view);
    if(getPresenter() != null) getPresenter().onDetach();
  }

  @Override protected void onDestroyView(@NonNull View view) {
    super.onDestroyView(view);
    if(getPresenter() != null) getPresenter().onDestroyView();
    unbinder.unbind();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if(getPresenter() != null) getPresenter().onDestroy();
    presenterPool.removePresenter(getPresenterTag());
  }

  public abstract @LayoutRes int getLayoutResId();
  public abstract Presenter getPresenter();
  public abstract String getPresenterTag();
  public abstract void setPresenter(Presenter presenter);

}
