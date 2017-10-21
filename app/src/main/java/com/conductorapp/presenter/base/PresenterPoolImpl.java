package com.conductorapp.presenter.base;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class PresenterPoolImpl implements PresenterPool {
  private Map<String, Presenter> pool = new HashMap<>();

  @Inject PresenterPoolImpl() {
  }

  @Override public void addPresenter(String tag, Presenter presenter) {
    if(!hasPresenter(tag)) pool.put(tag, presenter);
  }

  @Override public boolean hasPresenter(String tag) {
    return pool.containsKey(tag);
  }

  @Override public Presenter getPresenter(String tag) {
    return pool.get(tag);
  }

  @Override public void removePresenter(String tag) {
    pool.remove(tag);
  }
}
