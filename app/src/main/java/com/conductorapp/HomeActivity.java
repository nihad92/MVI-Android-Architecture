package com.conductorapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.conductorapp.controller.SearchController;

public class HomeActivity extends AppCompatActivity {
  private Router router;
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_home);

    ViewGroup container = (ViewGroup)findViewById(R.id.controller_container);

    router = Conductor.attachRouter(this, container, savedInstanceState);
    if (!router.hasRootController()) {
      router.setRoot(RouterTransaction.with(new SearchController()));
    }
  }


  @Override public void onBackPressed() {
    if(!router.handleBack()) {
      super.onBackPressed();
    }
  }
}
