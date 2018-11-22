package com.image.finder.components;

import com.image.finder.FlickrActivity;
import com.image.finder.modules.AppModule;
import com.image.finder.modules.NetModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetModule.class, AppModule.class})
public interface AppComponent {
    void inject(FlickrActivity application);
}
