package com.image.finder;

import android.widget.Toast;

import com.image.finder.models.PhotoPayload;
import com.image.finder.retrofit.FlickrApiService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.image.finder.FlickrActivity.isRequested;
import static com.image.finder.FlickrActivity.mTotalPages;
import static com.uber.autodispose.AutoDispose.autoDisposable;
import static com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider.from;

public class FlickrController {
    private static final String API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736";
    private static final String ERROR_TEXT = "Connected to the internet? Try again";
    private static final String FORMAT = "json";
    private static final String METHOD = "flickr.photos.search";

    private static final int CALLBACK = 1;

    private FlickrActivity mFlickrActivity;
    private FlickrApiService mFlickrApiService;
    private PhotoAdapter mPhotoAdapter;

    public FlickrController(FlickrActivity activity,
                            FlickrApiService apiService,
                            PhotoAdapter photoAdapter) {
        mFlickrApiService = apiService;
        mFlickrActivity = activity;
        mPhotoAdapter = photoAdapter;
    }

    public void getListOfPhotos(String textRequest, int pageNumber) {
        mFlickrApiService.listObjects(METHOD,
                API_KEY, FORMAT, CALLBACK,
                textRequest, pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(autoDisposable(from(mFlickrActivity)))
                .subscribe(this::handlerRequest,
                        throwable -> Toast.makeText(mFlickrActivity,
                                ERROR_TEXT, Toast.LENGTH_LONG).show());
    }

    private void handlerRequest(PhotoPayload photoPayload) {
        mPhotoAdapter.updatingPhotoAdapter(photoPayload.getPhotos().getPhoto(), isRequested);

        if(!isRequested) {
            isRequested = true;
            mTotalPages = photoPayload.getPhotos().getPages();
            mFlickrActivity.getPhotoRecyclerView().setAdapter(mPhotoAdapter);
        }
    }
}
