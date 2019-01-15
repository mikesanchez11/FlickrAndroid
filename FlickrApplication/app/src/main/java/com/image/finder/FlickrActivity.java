package com.image.finder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.image.finder.components.AppComponent;
import com.image.finder.retrofit.FlickrApiService;

import java.lang.annotation.Retention;

import javax.inject.Inject;
import javax.inject.Scope;

import dagger.Provides;

import static java.lang.annotation.RetentionPolicy.CLASS;

public class FlickrActivity extends AppCompatActivity {
    public static final String TAG = "Michael";
    @Inject
    GridLayoutManager mGridLayoutManager;
    @Inject
    FlickrController mController;
    @Inject
    FlickrScrollListener mFlickrListener;
    @Inject
    PhotoAdapter mPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr_layout);

        Component component = DaggerFlickrActivity_Component.builder()
                .appComponent(((FlickrApplication) getApplication()).getAppComponent())
                .module(new Module(this))
                .build();

        component.inject(this);

        RecyclerView photoRecyclerView = findViewById(R.id.photo_recycler_view);

        photoRecyclerView.setLayoutManager(mGridLayoutManager);
        photoRecyclerView.setAdapter(mPhotoAdapter);
        photoRecyclerView.addOnScrollListener(mFlickrListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Log.d(TAG, "onCreateMenuBefore");
        getMenuInflater().inflate(R.menu.flickr_search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        mController.observingUserInput(searchView);

        return true;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
    @FlickrActivityScope
    @dagger.Component(modules = Module.class, dependencies = AppComponent.class)
    interface Component {
        void inject(FlickrActivity flickrActivity);
    }

    @dagger.Module
    static class Module {
        private static final int SPAN_COUNT = 3;

        private FlickrActivity mFlickrActivity;

        Module(FlickrActivity activity) {
            mFlickrActivity = activity;
        }

        @FlickrActivityScope
        @Provides
        GridLayoutManager provideGridLayoutManager() {
            return new GridLayoutManager(mFlickrActivity, SPAN_COUNT);
        }

        @FlickrActivityScope
        @Provides
        PhotoAdapter providesPhotoAdapter() {
            return new PhotoAdapter();
        }

        @FlickrActivityScope
        @Provides
        FlickrController providesFlickrController(FlickrApiService apiService,
                                                  PhotoAdapter photoAdapter) {
            return new FlickrController(mFlickrActivity, apiService, photoAdapter);
        }

        @FlickrActivityScope
        @Provides
        FlickrScrollListener providesFlickrScrollListener(GridLayoutManager gridLayoutManager,
                                                          FlickrController controller) {
            return new FlickrScrollListener(
                    gridLayoutManager,
                    controller);
        }
    }
    @Scope
    @Retention(CLASS)
    @interface FlickrActivityScope {}
}
