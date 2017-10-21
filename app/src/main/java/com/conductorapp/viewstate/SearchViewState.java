package com.conductorapp.viewstate;

import com.conductorapp.viewstate.base.ViewState;
import conductor.com.data.models.GitUser;
import java.util.List;

public interface SearchViewState extends ViewState {

  final class Loading implements SearchViewState {
    private final boolean loading;

    public Loading(boolean loading) {
      this.loading = loading;
    }
  }

  final class Empty implements SearchViewState {
    private final String queryText;

    public Empty(String queryText) {
      this.queryText = queryText;
    }

    public String getQueryText() {
      return queryText;
    }
  }


  final class Error implements SearchViewState {
    private final String queryText;
    private final Throwable error;

    public Error(String queryText, Throwable error) {
      this.queryText = queryText;
      this.error = error;
    }

    public String getQueryText() {
      return queryText;
    }

    public Throwable getError() {
      return error;
    }
  }

  final class SearchResult implements SearchViewState {
    String queryText;
    List<GitUser> gitUsers;

    public SearchResult(String queryText, List<GitUser> gitUsers) {
      this.queryText = queryText;
      this.gitUsers = gitUsers;
    }

    public String getQueryText() {
      return queryText;
    }

    public void setQueryText(String queryText) {
      this.queryText = queryText;
    }

    public List<GitUser> getGitUsers() {
      return gitUsers;
    }

    public void setGitUsers(List<GitUser> gitUsers) {
      this.gitUsers = gitUsers;
    }
  }
}
