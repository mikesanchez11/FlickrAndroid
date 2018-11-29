package com.image.finder.components;

import com.image.finder.FlickrApplication;
import com.image.finder.modules.AppModule;
import com.image.finder.modules.NetModule;
import com.image.finder.retrofit.FlickrApiService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetModule.class, AppModule.class})
public interface AppComponent {
    void inject(FlickrApplication application);

    FlickrApiService flickrApiService();
}
