package com.image.finder;

import android.support.v7.widget.RecyclerView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class FlickrActivityTest {

    private FlickrActivity flickrActivity;
    private RecyclerView recyclerView;

    @Mock
    private FlickrController controller;

    @Before
    public void setup() {
        flickrActivity = Robolectric.buildActivity(FlickrActivity.class).create().visible().get();
        recyclerView = flickrActivity.findViewById(R.id.photo_recycler_view);
    }

    @Test
    public void onCreate_shouldSetRecyclerView() {
        assertNotNull(recyclerView.getAdapter());
        assertNotNull(recyclerView.getLayoutManager());
    }

    public void onCreateOptionMenu_shouldCallControllerObservingUsingInput() {
        shadowOf(flickrActivity).getOptionsMenu().findItem(R.id.menu_item_search);
    }
}