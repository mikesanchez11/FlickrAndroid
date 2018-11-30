package com.image.finder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.image.finder.components.AppComponent;
import com.image.finder.retrofit.FlickrApiService;
import com.jakewharton.rxbinding2.widget.RxSearchView;

import javax.inject.Inject;

import dagger.Provides;

public class FlickrActivity extends AppCompatActivity {
    private static final int SPAN_COUNT = 3;
    private static final int INITIAL_PAGE_NUMBER = 1;

    static boolean isRequested = false;
    int mFirstVisibleItemPosition;
    private int mPageNumber;
    static int mTotalPages;
    int mTotalItemCount;
    int mVisibleItemCount;
    private RecyclerView mPhotoRecyclerView;
    private String mTextRequest;

    @Inject
    FlickrController mController;
    @Inject
    PhotoAdapter mPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr_layout);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT); // inject this one

        mPageNumber = INITIAL_PAGE_NUMBER;
        mPhotoRecyclerView = findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(gridLayoutManager);

        //rxrecycler  
        mPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mVisibleItemCount = gridLayoutManager.getChildCount();
                mTotalItemCount = gridLayoutManager.getItemCount();
                mFirstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

                if (mVisibleItemCount + mFirstVisibleItemPosition >= mTotalItemCount) { // amount of items that are being shown plus the previous items displayed
                    if (mPageNumber <= mTotalPages) {
                        //getListOfPhotos(mTextRequest, mTotalPages);
                        mPageNumber++;
                        mTotalItemCount = gridLayoutManager.getItemCount();
                    }
                }
            }
        });

        Component component = DaggerFlickrActivity_Component.builder()
                .appComponent(((FlickrApplication) getApplication()).getAppComponent())
                .module(new Module(this))
                .build();

        component.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.flickr_search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        observingUserInput(searchView);

        return true;
    }

    private void observingUserInput(SearchView searchView) {
        RxSearchView.queryTextChangeEvents(searchView)
                .filter(charSequence -> charSequence.isSubmitted())
                .distinctUntilChanged()
                .subscribe(requestText -> {
                        mPageNumber = INITIAL_PAGE_NUMBER;
                        isRequested = false;
                        mController.getListOfPhotos(requestText.queryText().toString(), mPageNumber);
                });
    }

    public RecyclerView getPhotoRecyclerView() {
        return mPhotoRecyclerView;
    }

    @FlickrActivityScope
    @dagger.Component(modules = Module.class, dependencies = AppComponent.class)
    interface Component {
        void inject(FlickrActivity flickrActivity);
    }

    @dagger.Module
    static class Module {
        FlickrActivity mFlickrActivity;

        public Module(FlickrActivity activity) {
            mFlickrActivity = activity;
        }

        @Provides
        PhotoAdapter providesPhotoAdapter() {
            return new PhotoAdapter();
        }

        @Provides
        FlickrController providesFlickrController(FlickrApiService apiService, PhotoAdapter photoAdapter) {
            return new FlickrController(mFlickrActivity, apiService, photoAdapter);
        }
    }
}
