package com.image.finder.retrofit;

import com.image.finder.models.PhotoPayload;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrApiService {
    @GET("services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1")
    Observable<PhotoPayload> listObjects(@Query("text") String text,
                                         @Query("page") int pageNo);
}

