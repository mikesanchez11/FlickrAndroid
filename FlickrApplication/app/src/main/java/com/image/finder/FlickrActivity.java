package com.image.finder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.image.finder.components.AppComponent;
import com.image.finder.components.DaggerAppComponent;
import com.image.finder.models.PhotoPayload;
import com.image.finder.modules.AppModule;
import com.image.finder.modules.NetModule;
import com.image.finder.retrofit.FlickrApiService;
import com.jakewharton.rxbinding2.widget.RxSearchView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.uber.autodispose.AutoDispose.autoDisposable;
import static com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider.from;

public class FlickrActivity extends AppCompatActivity {
    private static final int CALLBACK = 1;
    private static final int SPAN_COUNT = 3;
    private static final int INITIAL_PAGE_NUMBER = 1;
    private static final String API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736";
    private static final String BASE_URL = "https://api.flickr.com/";
    private static final String ERROR_TEXT = "Connected to the internet? Try again";
    private static final String FORMAT = "json";
    private static final String METHOD = "flickr.photos.search";

    AppComponent mComponent;
    boolean isRequested = false;
    int mFirstVisibleItemPosition;
    private int mPageNumber;
    private int mTotalPages;
    int mTotalItemCount;
    int mVisibleItemCount;
    private RecyclerView mPhotoRecyclerView;
    private String mTextRequest;

    @Inject
    FlickrApiService mApiService;
    @Inject
    Context mContext;
    @Inject
    PhotoAdapter mPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr_layout);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, SPAN_COUNT);

        mComponent = DaggerAppComponent.builder()
                .netModule(new NetModule(BASE_URL))
                .appModule(new AppModule(getApplicationContext()))
                .build();

        mPageNumber = INITIAL_PAGE_NUMBER;
        mPhotoRecyclerView = findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(gridLayoutManager);
        mPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mVisibleItemCount = gridLayoutManager.getChildCount();
                mTotalItemCount = gridLayoutManager.getItemCount();
                mFirstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

                if (mVisibleItemCount + mFirstVisibleItemPosition >= mTotalItemCount) { // amount of items that are being shown plus the previous items displayed
                    if (mPageNumber <= mTotalPages) {
                        apiRequest();
                        mPageNumber++;
                        mTotalItemCount = gridLayoutManager.getItemCount();
                    }
                }
            }
        });

        Stetho.initializeWithDefaults(this);

        getComponent().inject(this);
    }

    public AppComponent getComponent() {
        return mComponent;
    }

    public void apiRequest() {
        mApiService.listObjects(METHOD,
                API_KEY, FORMAT, CALLBACK,
                mTextRequest, mPageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(autoDisposable(from(this)))
                .subscribe(this::handlerRequest,
                        throwable -> Toast.makeText(mContext, ERROR_TEXT, Toast.LENGTH_LONG).show());
    }

    private void handlerRequest(PhotoPayload photoPayload) {
        if(!isRequested) {
            mPhotoAdapter.updatingPhotoAdapter(
                    photoPayload.getPhotos().getPhoto(), mContext);
            isRequested = true;
            mTotalPages = photoPayload.getPhotos().getPages();
            mPhotoRecyclerView.setAdapter(mPhotoAdapter);
        } else {
            mPhotoAdapter.addMoreItems(photoPayload.getPhotos().getPhoto());
        }
    }

    public void setTextRequest(String textRequest) {
        mTextRequest = textRequest;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.flickr_search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        RxSearchView.queryTextChangeEvents(searchView)
                .filter(charSequence -> charSequence.isSubmitted())
                .distinctUntilChanged()
                .subscribe(requestText -> {
                        setTextRequest(requestText.queryText().toString());
                        mPageNumber = INITIAL_PAGE_NUMBER;
                        isRequested = false;
                        apiRequest();
                });
        return true;
    }
}
