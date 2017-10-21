package com.conductorapp.di.modules;

import android.content.Context;
import com.conductorapp.AndroidApplication;
import com.conductorapp.presenter.base.PresenterPool;
import com.conductorapp.presenter.base.PresenterPoolImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class AppModule {
  private AndroidApplication androidApplication;

  public AppModule(AndroidApplication androidApplication) {
    this.androidApplication = androidApplication;
  }

  @Provides @Singleton Context providesContext() {return androidApplication;}
  @Provides @Singleton PresenterPool presenterPool(PresenterPoolImpl presenterPool) { return presenterPool; }
}
