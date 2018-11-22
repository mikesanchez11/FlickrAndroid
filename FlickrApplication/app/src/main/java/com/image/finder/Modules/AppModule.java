package com.image.finder.modules;

import android.content.Context;

import com.image.finder.PhotoAdapter;

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
    Context providesContext() {
        return mContext;
    }

    @Provides
    @Singleton
    PhotoAdapter providesPhotoAdapter() {
        return new PhotoAdapter();
    }
}
