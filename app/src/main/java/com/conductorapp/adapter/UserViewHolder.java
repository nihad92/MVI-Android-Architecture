package com.conductorapp.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.conductorapp.R;
import conductor.com.data.models.GitUser;

public class UserViewHolder extends RecyclerView.ViewHolder {
  @BindView(R.id.username) AppCompatTextView username;
  public UserViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  public void bind(GitUser gitUser) {
    username.setText(gitUser.getUsername());
  }
}
