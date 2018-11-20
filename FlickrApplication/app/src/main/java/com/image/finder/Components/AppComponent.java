package com.image.finder.Components;

import com.image.finder.FlickrActivity;
import com.image.finder.Modules.AppModule;
import com.image.finder.Modules.NetModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetModule.class, AppModule.class})
public interface AppComponent {
    void inject(FlickrActivity application);
}
