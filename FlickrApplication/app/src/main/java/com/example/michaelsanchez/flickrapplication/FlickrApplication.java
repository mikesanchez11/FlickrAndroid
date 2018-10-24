package com.example.michaelsanchez.flickrapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.michaelsanchez.flickrapplication.Components.AppComponent;
import com.example.michaelsanchez.flickrapplication.Components.DaggerAppComponent;
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

public class FlickrApplication extends AppCompatActivity {
    private static final int SPAN_COUNT = 3;

    AppComponent mComponent;
    CompositeDisposable mDisposables = new CompositeDisposable();
    RecyclerView mPhotoRecyclerView;
    String mTextRequest;

    @Inject
    FlickrApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr_application);

        mPhotoRecyclerView = findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), SPAN_COUNT));

        mPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        Stetho.initializeWithDefaults(this);

        mComponent = DaggerAppComponent.builder()
                .netModule(new NetModule(FlickrApiService.BASE_URL))
                .build();

        getComponent().inject(this);
    }

    public AppComponent getComponent() {
        return mComponent;
    }

    public void apiRequest() {
        Disposable disposable =
                mApiService.listObjects(FlickrApiService.method,
                        FlickrApiService.API_KEY, FlickrApiService.format, FlickrApiService.callback,
                        mTextRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(flickrObject -> mPhotoRecyclerView.setAdapter(new PhotoAdapter(flickrObject.getPhotos().getPhoto())));
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
                Log.d("TAG", "QueryTextSubmit: " + s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                setTextRequest(s);
                apiRequest();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("TAG", "QueryTextChange: " + s);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = QueryPreferences.getStoredQuery(getApplicationContext());
                searchView.setQuery(query, false);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                QueryPreferences.setStoredQuery(getApplicationContext(), null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<Photo> mPhotoItems;
        public PhotoAdapter(List<Photo> galleryItems) {
            mPhotoItems = galleryItems;
        }
        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View view = inflater.inflate(R.layout.list_item_gallery, viewGroup, false);
            return new PhotoHolder(view);
        }
        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            Photo photoItem = mPhotoItems.get(position);
            photoHolder.loadImage(photoItem);
        }
        @Override
        public int getItemCount() {
            return mPhotoItems.size();
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image_view);
        }

        public void loadImage(Photo photo) {
            String imagePath;

            imagePath = "http://farm"
                    + Integer.toString(photo.getFarm())
                    + ".static.flickr.com/"
                    + photo.getServer() + "/" + photo.getId() + "_"
                    + photo.getSecret() + ".jpg";

            Log.d("TAG", imagePath);
            Picasso.get().load(imagePath).into(mImageView);
        }
    }
}
