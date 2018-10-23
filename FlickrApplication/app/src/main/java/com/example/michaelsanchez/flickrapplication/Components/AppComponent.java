package com.example.michaelsanchez.flickrapplication.Components;

import com.example.michaelsanchez.flickrapplication.FlickrApplication;
import com.example.michaelsanchez.flickrapplication.Modules.NetModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = NetModule.class)
public interface AppComponent {
    void inject(FlickrApplication application);
}
