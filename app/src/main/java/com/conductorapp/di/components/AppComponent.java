package com.conductorapp.di.components;

import android.content.Context;
import com.conductorapp.di.modules.AppModule;
import com.conductorapp.presenter.base.PresenterPool;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = AppModule.class)
public interface AppComponent {
  Context context();
  PresenterPool presenterPool();
}
