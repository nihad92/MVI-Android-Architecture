package com.conductorapp.presenter.base;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

class DisposableViewStateObserver<VS> extends DisposableObserver<VS> {
  private final BehaviorSubject<VS> subject;

  public DisposableViewStateObserver(BehaviorSubject<VS> subject) {
    this.subject = subject;
  }

  @Override public void onNext(VS value) {
    subject.onNext(value);
  }

  @Override public void onError(Throwable e) {
    throw new IllegalStateException(
        "ViewState observable must not reach error state - onError()", e);
  }

  @Override public void onComplete() {
    // ViewState observable never completes so ignore any complete event
  }
}
