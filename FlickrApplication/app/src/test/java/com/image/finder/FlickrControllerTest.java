package com.image.finder;

import com.image.finder.retrofit.FlickrApiService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FlickrControllerTest {

    @Mock
    FlickrActivity mFlickrActivity;
    @Mock
    FlickrApiService mApiService;
    @Mock
    PhotoAdapter mPhotoAdapter;

    private FlickrController mFlickrController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mFlickrController = new FlickrController(mFlickrActivity, mApiService, mPhotoAdapter);
    }

//    @Test
//    private void observingUserInput_shouldGetListOfPhotos() {
//        mFlickrController.observingUserInput();
//    }

    @Test
    public void handleScroll_shouldCallLoadImages_when() {

    }
}