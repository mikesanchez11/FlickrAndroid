package com.example.michaelsanchez.flickrapplication.Components;

import com.example.michaelsanchez.flickrapplication.FlickrActivity;
import com.example.michaelsanchez.flickrapplication.Modules.AppModule;
import com.example.michaelsanchez.flickrapplication.Modules.NetModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetModule.class, AppModule.class})
public interface AppComponent {
    void inject(FlickrActivity application);
}
