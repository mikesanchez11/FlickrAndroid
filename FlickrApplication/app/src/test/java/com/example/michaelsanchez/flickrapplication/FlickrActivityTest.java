package com.example.michaelsanchez.flickrapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.michaelsanchez.flickrapplication.Services.FlickrApiService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.when;

public class FlickrActivityTest {

    FlickrActivity flickrActivity;
    @Mock
    FlickrApiService apiService;
    @Mock
    Context context;
    @Mock
    PhotoAdapter photoAdapter;
    @Mock
    PhotoHolder photoHolder;

    @Mock
    FlickrObject flickrObject;
    @Mock
    RecyclerView recyclerView;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        flickrActivity = new FlickrActivity();

        when(apiService.listObjects(anyString()
                ,anyString(), anyString(),1,
                anyString(), 1))
                .thenReturn(Observable.just(flickrObject));
    }

    @Test
    public void apiRequest_shouldSetadapter() {

    }
}