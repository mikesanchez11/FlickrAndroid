package com.image.finder.retrofit;

import com.image.finder.models.PhotoPayload;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrApiService {
    @GET("services/rest/")
    Observable<PhotoPayload> listObjects(@Query("method") String method,
                                         @Query("api_key") String API_KEY,
                                         @Query("format") String format,
                                         @Query("nojsoncallback") int callback,
                                         @Query("text") String text,
                                         @Query("page") int pageNo);
}

