package com.image.finder;

import android.app.Application;

import com.image.finder.components.AppComponent;
import com.image.finder.components.DaggerAppComponent;
import com.image.finder.modules.AppModule;
import com.image.finder.modules.NetModule;

public class FlickrApplication extends Application {
    private static final String BASE_URL = "https://api.flickr.com/";
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .netModule(new NetModule(BASE_URL))
                .build();

        mAppComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
