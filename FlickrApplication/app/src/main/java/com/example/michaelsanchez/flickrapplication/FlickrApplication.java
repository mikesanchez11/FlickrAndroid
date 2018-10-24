package com.example.michaelsanchez.flickrapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.michaelsanchez.flickrapplication.Components.AppComponent;
import com.example.michaelsanchez.flickrapplication.Components.DaggerAppComponent;
import com.example.michaelsanchez.flickrapplication.Modules.NetModule;
import com.example.michaelsanchez.flickrapplication.Services.FlickrApiService;
import com.facebook.stetho.Stetho;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlickrApplication extends AppCompatActivity {

    AppComponent component;
    String mTextRequest;
    @Inject
    FlickrApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr_application);

        Stetho.initializeWithDefaults(this);

        component = DaggerAppComponent.builder()
                .netModule(new NetModule(FlickrApiService.BASE_URL))
                .build();

        getComponent().inject(this);

        mTextRequest = "Dogs";

    }

    public AppComponent getComponent() {
        return component;
    }

    public void apiRequest() {
        Call<FlickrObject> call = apiService.listObjects(FlickrApiService.method,
                FlickrApiService.API_KEY, FlickrApiService.format, FlickrApiService.callback,
                mTextRequest);

        call.enqueue(new Callback<FlickrObject>() {
            @Override
            public void onResponse(Call<FlickrObject> call, Response<FlickrObject> response) {
                FlickrObject flickrObject = response.body();

                Log.d("TAG", Integer.toString(response.code()));
                Log.d("TAG", flickrObject.getPhotos().getPhoto().get(1).getTitle());
            }

            @Override
            public void onFailure(Call<FlickrObject> call, Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });
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
    public void setTextRequest(String textRequest) {
        mTextRequest = textRequest;
    }
}
