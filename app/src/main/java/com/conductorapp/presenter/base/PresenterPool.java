package com.conductorapp.presenter.base;

import com.conductorapp.view.base.BaseView;
import javax.inject.Singleton;

@Singleton
public interface PresenterPool {
  void addPresenter(String tag, Presenter presenter);
  boolean hasPresenter(String tag);
  Presenter getPresenter(String tag);
  void removePresenter(String tag);
}
