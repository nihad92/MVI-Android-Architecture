package conductor.com.data.repository;

import io.reactivex.Observable;

public abstract class AbstractDataSource<A, B> {
  public final Observable<A> fetch(B payload) {
    return fetchData(payload);
  }

  protected abstract Observable<A> fetchData(B payload);
}
