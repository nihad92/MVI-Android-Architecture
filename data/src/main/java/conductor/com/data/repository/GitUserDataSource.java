package conductor.com.data.repository;

import conductor.com.data.models.GitUser;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class GitUserDataSource extends AbstractDataSource<List<GitUser>, String> {
  List<GitUser> gitUserList = null;
  @Inject GitUserDataSource() {
    gitUserList = Arrays.asList(new GitUser("nihad"), new GitUser("adnxy"), new GitUser("vedo102"), new GitUser("menilv"));
  }

  @Override protected Observable<List<GitUser>> fetchData(String payload) {
    return Observable.zip(Observable.timer(2000, TimeUnit.MILLISECONDS),
        Observable.just(gitUserList), (x, y) -> y)
        .subscribeOn(Schedulers.io())
        .map(x -> {
          List<GitUser> array = new ArrayList<GitUser>();
          for (GitUser gitUser: x) {
            if(gitUser.getUsername().contains(payload)) array.add(gitUser);
          }
          return array;
        })
        .observeOn(AndroidSchedulers.mainThread());
  }
}
