package com.conductorapp.presenter.base;

import android.support.annotation.NonNull;
import com.conductorapp.view.base.BaseView;

public interface Presenter<V extends BaseView> {
  void onAttach(@NonNull V view);
  void onDetach();
  void onDestroyView();
  void onDestroy();
}
