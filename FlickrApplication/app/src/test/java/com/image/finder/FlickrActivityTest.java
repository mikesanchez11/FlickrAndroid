package com.image.finder;

import android.support.v7.widget.RecyclerView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class FlickrActivityTest {

    private RecyclerView recyclerView;

    @Before
    public void setup() {
        FlickrActivity flickrActivity = Robolectric.setupActivity(FlickrActivity.class);
        recyclerView = flickrActivity.findViewById(R.id.photo_recycler_view);
    }

    @Test
    public void onCreate_shouldSetRecyclerView() {
        assertNotNull(recyclerView.getAdapter());
        assertNotNull(recyclerView.getLayoutManager());
    }
}