package com.example.michaelsanchez.flickrapplication.Services;

import com.example.michaelsanchez.flickrapplication.FlickrObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrApiService {
    String API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736";
    String BASE_URL = "https://api.flickr.com/";
    int callback = 1;
    String format = "json";
    String method = "flickr.photos.search";

    @GET("services/rest/")
    Observable<FlickrObject> listObjects(@Query("method") String method,
                                         @Query("api_key") String API_KEY,
                                         @Query("format") String format,
                                         @Query("nojsoncallback") int callback,
                                         @Query("text") String text,
                                         @Query("page") int pageNo);
}

