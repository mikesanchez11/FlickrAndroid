package com.example.michaelsanchez.flickrapplication.Modules;

import android.content.Context;

import com.example.michaelsanchez.flickrapplication.PhotoAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class AppModule {
    Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    CompositeDisposable providesCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    @Singleton
    Context providesContext() {
        return mContext;
    }

    @Provides
    @Singleton
    PhotoAdapter providesPhotoAdapter() {
        return new PhotoAdapter();
    }
}
