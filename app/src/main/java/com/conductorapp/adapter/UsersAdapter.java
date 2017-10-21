package com.conductorapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.conductorapp.R;
import conductor.com.data.models.GitUser;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class UsersAdapter extends RecyclerView.Adapter<UserViewHolder> {
  List<GitUser>  gitUserList = new ArrayList<>();

  @Inject public UsersAdapter() {

  }

  @Override public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new UserViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.git_user_item, parent, false));
  }

  @Override public void onBindViewHolder(UserViewHolder holder, int position) {
    holder.bind(gitUserList.get(position));
  }

  @Override public int getItemCount() {
    return gitUserList.size();
  }

  public void setUsers(List<GitUser> gitUsers) {
    gitUserList = gitUsers;
    notifyDataSetChanged();
  }
}
