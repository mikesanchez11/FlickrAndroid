package com.image.finder;

import android.app.Application;
import android.util.Log;

import com.image.finder.components.AppComponent;
import com.image.finder.components.DaggerAppComponent;
import com.image.finder.modules.AppModule;
import com.image.finder.modules.NetModule;

import static com.image.finder.FlickrActivity.TAG;

public class FlickrApplication extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .netModule(new NetModule())
                .build();

        mAppComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
