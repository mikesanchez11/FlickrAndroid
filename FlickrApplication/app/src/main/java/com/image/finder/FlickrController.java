package com.image.finder;

import android.annotation.SuppressLint;
import android.support.annotation.VisibleForTesting;
import android.widget.SearchView;
import android.widget.Toast;

import com.image.finder.models.PhotoPayload;
import com.image.finder.models.Photos;
import com.image.finder.retrofit.FlickrApiService;
import com.jakewharton.rxbinding2.widget.RxSearchView;
import com.jakewharton.rxbinding2.widget.SearchViewQueryTextEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.uber.autodispose.AutoDispose.autoDisposable;
import static com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider.from;

class FlickrController {
    private static final int INITIAL_PAGE_NUMBER = 1;

    @VisibleForTesting
    static final String ERROR_TEXT = "Connected to the internet? Try again";

    private boolean mIsRequested = false;
    private int mPageNumber;
    private int mTotalPages;
    private String mTextRequest;

    private FlickrActivity mFlickrActivity;
    private FlickrApiService mFlickrApiService;
    private PhotoAdapter mPhotoAdapter;

    FlickrController(FlickrActivity activity,
                     FlickrApiService apiService,
                     PhotoAdapter photoAdapter) {
        mFlickrApiService = apiService;
        mFlickrActivity = activity;
        mPhotoAdapter = photoAdapter;
        mPageNumber = INITIAL_PAGE_NUMBER;
    }

    void handleScroll() {
        mPageNumber++;
        if (mPageNumber <= mTotalPages) {
            if (mIsRequested) {
                getListOfPhotos(mTextRequest, mPageNumber);
            }
        }
    }

    @SuppressLint("CheckResult")
    void observingUserInput(SearchView searchView) {
        RxSearchView.queryTextChangeEvents(searchView)
                .filter(SearchViewQueryTextEvent::isSubmitted)
                .map(queryTextEvent -> queryTextEvent.queryText().toString())
                .distinctUntilChanged()
                .as(autoDisposable(from(mFlickrActivity)))
                .subscribe(textRequest -> {
                    setTextRequest(textRequest);
                    mPhotoAdapter.clear();
                    getListOfPhotos(textRequest, INITIAL_PAGE_NUMBER);});
    }

    private void getListOfPhotos(String textRequest, int pageNumber) {
        mFlickrApiService.listObjects(
                textRequest, pageNumber)
                .subscribeOn(Schedulers.io())
                .doOnNext(photoPayload -> mIsRequested = true)
                .doOnComplete(() -> mIsRequested = true)
                .doOnError(throwable -> mIsRequested = false)
                .observeOn(AndroidSchedulers.mainThread())
                .as(autoDisposable(from(mFlickrActivity)))
                .subscribe(this::handlerRequest,
                        throwable -> Toast.makeText(mFlickrActivity,
                                ERROR_TEXT, Toast.LENGTH_LONG).show());
    }

    private void handlerRequest(PhotoPayload photoPayload) {
        Photos photos = photoPayload.getPhotos();
        mPhotoAdapter.updatingPhotoAdapter(photos.getPhoto());
        mTotalPages = photos.getPages();
    }

    private void setTextRequest(String mTextRequest) {
        this.mTextRequest = mTextRequest;
    }
}
