package com.conductorapp.di.components;

import com.conductorapp.controller.SearchController;
import com.conductorapp.di.PerController;
import dagger.Component;

@PerController @Component(dependencies = AppComponent.class)
public interface ControllerBaseComponent {
  void inject(SearchController controller);
}
