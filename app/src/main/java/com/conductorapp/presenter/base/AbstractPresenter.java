
package com.conductorapp.presenter.base;
/*
import android.util.Pair;
import com.conductorapp.view.base.BaseView;
import com.conductorapp.viewstate.base.ViewState;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public abstract class AbstractPresenter<V extends BaseView, VS extends ViewState> {
  private boolean firstTimeAttached = true;
  private V view;

  CompositeSubscription intentSubscriptions = new CompositeSubscription();
  Subscription viewStateSubscription;
  Subscription viewStateSubjectSubscription;
  private Action1<VS> onNextAction;

  private List<IntentBinder<?>> intentBinders = new ArrayList<>();
  private BehaviorSubject<VS> viewStateBehaviourSubject = BehaviorSubject.create();

  private final static class IntentBinder<I> {
    private final PublishSubject<I> publishSubject;
    private final Observable<I> observable;

    public IntentBinder(PublishSubject<I> publishSubject, Observable<I> observable) {

      this.publishSubject = publishSubject;
      this.observable = observable;
    }

    public PublishSubject<I> getIntentPublishSubject() {
      return publishSubject;
    }

    public Observable<I> getIntentObservable() {
      return observable;
    }
  }

  protected V getView() {
    return view;
  }

  public final void onAttach(V view) {
    this.view = view;
    if(firstTimeAttached) {
      bindIntents();
    }

    if(onNextAction != null) {
      viewStateSubscription = viewStateBehaviourSubject.subscribe(onNextAction);
    }

    for (int i = 0; i < intentBinders.size(); i++) {
      IntentBinder intentBinder = intentBinders.get(i);
      intentSubscriptions.add(intentBinder.getIntentObservable().subscribe(intentBinder.getIntentPublishSubject()));
    }

    firstTimeAttached = false;
  }

  public final void onDetach() {
    intentSubscriptions.clear();
    viewStateSubscription.unsubscribe();
    viewStateSubscription = null;
  }

  public final void onDestroyView() {
  }

  public final void onDestroy() {
    viewStateSubjectSubscription.unsubscribe();
    viewStateSubjectSubscription = null;
  }
  protected abstract void bindIntents();

  protected <I> Observable<I> intent(Observable<I> observable) {
    PublishSubject<I> intentSubject = PublishSubject.create();
    intentBinders.add(new IntentBinder<>(intentSubject, observable));
    return intentSubject;
  }

  protected void subscribeViewState(Observable<VS> observable, Action1<VS> onNextAction) {
    this.onNextAction = onNextAction;
    viewStateSubjectSubscription = observable.subscribe(viewStateBehaviourSubject);
  }

  private void reset() {
    firstTimeAttached = false;
  }
}
*/
/*
 * Copyright 2017 Hannes Dorfmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import com.conductorapp.view.base.BaseView;
import com.conductorapp.viewstate.base.ViewState;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPresenter<V extends BaseView, VS extends ViewState> implements Presenter<V> {


  protected interface ViewIntentBinder<V extends BaseView, I> {
    @NonNull public Observable<I> bind(@NonNull V view);
  }

  protected interface ViewStateConsumer<V extends BaseView, VS> {
    public void accept(@NonNull V view, @NonNull VS viewState);
  }

  private class IntentRelayBinderPair<I> {
    private final PublishSubject<I> intentRelaySubject;
    private final ViewIntentBinder<V, I> intentBinder;

    public IntentRelayBinderPair(PublishSubject<I> intentRelaySubject,
        ViewIntentBinder<V, I> intentBinder) {
      this.intentRelaySubject = intentRelaySubject;
      this.intentBinder = intentBinder;
    }
  }

  private final BehaviorSubject<VS> viewStateBehaviorSubject;
  private boolean subscribeViewStateMethodCalled = false;
  private List<IntentRelayBinderPair<?>> intentRelaysBinders = new ArrayList<>(4);
  private CompositeDisposable intentDisposals;
  private Disposable viewRelayConsumerDisposable;
  private Disposable viewStateDisposable;
  private boolean viewAttachedFirstTime = true;
  private V view;

  private ViewStateConsumer<V, VS> viewStateConsumer;

  /**
   * Creates a new AbstractPresenter without an initial view state
   */
  public AbstractPresenter() {
    viewStateBehaviorSubject = BehaviorSubject.create();
    reset();
  }

  public AbstractPresenter(@NonNull VS initialViewState) {
    if (initialViewState == null) {
      throw new NullPointerException("Initial ViewState == null");
    }

    viewStateBehaviorSubject = BehaviorSubject.createDefault(initialViewState);
    reset();
  }

  protected Observable<VS> getViewStateObservable() {
    return viewStateBehaviorSubject;
  }

  protected V getView() {
    return view;
  }

  @Override public void onAttach(@NonNull V view) {
    this.view = view;
    if (viewAttachedFirstTime) {
      bindIntents();
    }

    if (viewStateConsumer != null) {
      subscribeViewStateConsumerActually(view);
    }

    int intentsSize = intentRelaysBinders.size();
    for (int i = 0; i < intentsSize; i++) {
      IntentRelayBinderPair<?> intentRelayBinderPair = intentRelaysBinders.get(i);
      bindIntentActually(view, intentRelayBinderPair);
    }


    viewAttachedFirstTime = false;
  }

  @Override public void onDetach() {
    if (viewRelayConsumerDisposable != null) {
      // Cancel subscription from View to viewState Relay
      viewRelayConsumerDisposable.dispose();
      viewRelayConsumerDisposable = null;
    }

    if (intentDisposals != null) {
      intentDisposals.dispose();
      intentDisposals = null;
    }
  }

  @Override public void onDestroyView() {

  }

  @Override public void onDestroy() {
    if (viewStateDisposable != null) {
      // Cancel the overall observable stream
      viewStateDisposable.dispose();
    }

    unbindIntents();
    reset();
  }

  private void reset() {
    viewAttachedFirstTime = true;
    intentRelaysBinders.clear();
    subscribeViewStateMethodCalled = false;
  }

  @MainThread protected void subscribeViewState(@NonNull Observable<VS> viewStateObservable,
      @NonNull ViewStateConsumer<V, VS> consumer) {
    if (subscribeViewStateMethodCalled) {
      throw new IllegalStateException(
          "subscribeViewState() method is only allowed to be called once");
    }
    subscribeViewStateMethodCalled = true;

    if (viewStateObservable == null) {
      throw new NullPointerException("ViewState Observable is null");
    }

    if (consumer == null) {
      throw new NullPointerException("ViewStateBinder is null");
    }

    this.viewStateConsumer = consumer;

    viewStateDisposable = viewStateObservable.subscribeWith(
        new DisposableViewStateObserver<>(viewStateBehaviorSubject));
  }

  @MainThread private void subscribeViewStateConsumerActually(@NonNull final V view) {

    if (view == null) {
      throw new NullPointerException("View is null");
    }

    if (viewStateConsumer == null) {
      throw new NullPointerException(ViewStateConsumer.class.getSimpleName()
          + " is null. This is a mosby internal bug. Please file an issue at https://github.com/sockeqwe/mosby/issues");
    }

    viewRelayConsumerDisposable = viewStateBehaviorSubject.subscribe(new Consumer<VS>() {
      @Override public void accept(VS vs) throws Exception {
        viewStateConsumer.accept(view, vs);
      }
    });
  }

  @MainThread abstract protected void bindIntents();

  protected void unbindIntents() {
  }

  @MainThread protected <I> Observable<I> intent(ViewIntentBinder<V, I> binder) {
    PublishSubject<I> intentRelay = PublishSubject.create();
    intentRelaysBinders.add(new IntentRelayBinderPair<I>(intentRelay, binder));
    return intentRelay;
  }

  @MainThread private <I> Observable<I> bindIntentActually(@NonNull V view,
      @NonNull IntentRelayBinderPair<?> relayBinderPair) {
    PublishSubject<I> intentRelay = (PublishSubject<I>) relayBinderPair.intentRelaySubject;
    ViewIntentBinder<V, I> intentBinder = (ViewIntentBinder<V, I>) relayBinderPair.intentBinder;
    Observable<I> intent = intentBinder.bind(view);

    if (intentDisposals == null) {
      intentDisposals = new CompositeDisposable();
    }

    intentDisposals.add(intent.subscribeWith(new DisposableIntentObserver<I>(intentRelay)));
    return intentRelay;
  }
}