package com.image.finder.Modules;

import com.image.finder.Services.FlickrApiService;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {
    String baseUrl;

    public NetModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        //see what the body contains for debugging
        //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        return client;
    }

    @Provides
    @Singleton
    Gson providesGson() {
        Gson gson = new GsonBuilder()
                .create();
        return gson;
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(Gson gson, OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    FlickrApiService providesFlickrApiService(Retrofit retrofit) {
        FlickrApiService apiService = retrofit.create(FlickrApiService.class);
        return apiService;
    }
}
