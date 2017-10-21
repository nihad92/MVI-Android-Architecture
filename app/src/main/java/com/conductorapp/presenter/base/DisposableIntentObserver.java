package com.conductorapp.presenter.base;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

class DisposableIntentObserver<I> extends DisposableObserver<I> {

  private final PublishSubject<I> subject;

  public DisposableIntentObserver(PublishSubject<I> subject) {
    this.subject = subject;
  }

  @Override public void onNext(I value) {
    subject.onNext(value);
  }

  @Override public void onError(Throwable e) {
    throw new IllegalStateException("View intents must not throw errors", e);
  }

  @Override public void onComplete() {
    subject.onComplete();
  }
}
