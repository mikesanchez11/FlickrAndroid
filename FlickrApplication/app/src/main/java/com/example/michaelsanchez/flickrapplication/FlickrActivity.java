package com.example.michaelsanchez.flickrapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.michaelsanchez.flickrapplication.Components.AppComponent;
import com.example.michaelsanchez.flickrapplication.Components.DaggerAppComponent;
import com.example.michaelsanchez.flickrapplication.Modules.AppModule;
import com.example.michaelsanchez.flickrapplication.Modules.NetModule;
import com.example.michaelsanchez.flickrapplication.Photos.Photo;
import com.example.michaelsanchez.flickrapplication.Services.FlickrApiService;
import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FlickrActivity extends AppCompatActivity {
    private static final int SPAN_COUNT = 3;
    private static final int INITIAL_PAGE_NUMBER = 1;

    AppComponent mComponent;
    boolean isRequested = false;
    GridLayoutManager mGridLayoutManager;
    int mFirstVisibleItemPosition;
    int mPageNumber;
    int mTotalItemCount;
    int mTotalPages;
    int mVisibleItemCount;
    RecyclerView mPhotoRecyclerView;
    String mTextRequest;

    @Inject
    FlickrApiService mApiService;
    @Inject
    CompositeDisposable mDisposables;
    @Inject
    Context mContext;
    @Inject
    PhotoAdapter mPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr_application);

        mComponent = DaggerAppComponent.builder()
                .netModule(new NetModule(FlickrApiService.BASE_URL))
                .appModule(new AppModule(getApplicationContext()))
                .build();
        
        mGridLayoutManager = new GridLayoutManager(mContext, SPAN_COUNT);
        mPageNumber = INITIAL_PAGE_NUMBER;
        mPhotoRecyclerView = findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(mGridLayoutManager);

        mPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mVisibleItemCount = mGridLayoutManager.getChildCount();
                mTotalItemCount = mGridLayoutManager.getItemCount();
                mFirstVisibleItemPosition = mGridLayoutManager.findFirstVisibleItemPosition();

                if (mVisibleItemCount + mFirstVisibleItemPosition >= mTotalItemCount) {
                    if (mPageNumber <= mTotalPages) {
                        apiRequest();
                        mPageNumber++;
                        mTotalItemCount = mGridLayoutManager.getItemCount();
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
        Disposable disposable =
                mApiService.listObjects(FlickrApiService.method,
                        FlickrApiService.API_KEY, FlickrApiService.format, FlickrApiService.callback,
                        mTextRequest, mPageNumber)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(flickrObject -> {
                            if(!isRequested) {
                                mPhotoAdapter.updatingPhotoAdapter(
                                        flickrObject.getPhotos().getPhoto(), mContext);
                                isRequested = true;
                                mTotalPages = flickrObject.getPhotos().getPages();
                                mPhotoRecyclerView.setAdapter(mPhotoAdapter);
                            } else {
                                mPhotoAdapter.addMoreItems(flickrObject.getPhotos().getPhoto());
                            }
                        });
        mDisposables.add(disposable);
    }

    public void setTextRequest(String textRequest) {
        mTextRequest = textRequest;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposables.dispose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.flickr_search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                setTextRequest(s);
                mPageNumber = INITIAL_PAGE_NUMBER;
                isRequested = false;
                apiRequest();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(v -> {
            String query = QueryPreferences.getStoredQuery(mContext);
            searchView.setQuery(query, false);
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                QueryPreferences.setStoredQuery(mContext, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
