package com.image.finder.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    Gson providesGson() {
        return new GsonBuilder()
                .create();
    }

    @Provides
    @Singleton
    Context providesContext() {
        return mContext;
    }
}
