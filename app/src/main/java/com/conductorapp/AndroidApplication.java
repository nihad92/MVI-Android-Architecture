package com.conductorapp;

import android.app.Application;
import com.conductorapp.di.components.AppComponent;
import com.conductorapp.di.components.DaggerAppComponent;
import com.conductorapp.di.modules.AppModule;

public class AndroidApplication extends Application {
  private AppComponent appComponent;
  @Override public void onCreate() {
    super.onCreate();
    appComponent = DaggerAppComponent.builder().appModule(new AppModule(this))
        .build();
  }

  public AppComponent getAppComponent() {
    return appComponent;
  }
}
