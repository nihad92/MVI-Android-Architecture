package com.conductorapp.view;

import com.conductorapp.view.base.BaseView;
import com.conductorapp.viewstate.SearchViewState;
import io.reactivex.Observable;

public interface SearchView extends BaseView {
  void render(SearchViewState viewState);
  Observable<CharSequence> searchIntent();
}
