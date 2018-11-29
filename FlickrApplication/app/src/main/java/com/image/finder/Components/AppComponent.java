package com.image.finder.components;

import android.app.Application;

import com.image.finder.modules.AppModule;
import com.image.finder.modules.NetModule;
import com.image.finder.retrofit.FlickrApiService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetModule.class, AppModule.class})
public interface AppComponent {
    void inject(Application application);

    FlickrApiService flickrApiService();
}
