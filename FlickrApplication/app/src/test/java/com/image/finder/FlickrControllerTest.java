package com.image.finder;

import android.widget.SearchView;

import com.image.finder.models.PhotoPayload;
import com.image.finder.retrofit.FlickrApiService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowToast;

import io.reactivex.Observable;

import static com.image.finder.FlickrController.ERROR_TEXT;
import static io.reactivex.Observable.just;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class FlickrControllerTest {

    private FlickrController mFlickrController;
    private SearchView mSearchView;

    @Mock
    private FlickrApiService mApiService;
    @Mock
    private PhotoAdapter mPhotoAdapter;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        FlickrActivity flickrActivity = Robolectric.setupActivity(FlickrActivity.class);
        mSearchView = new SearchView(flickrActivity);
        mFlickrController = new FlickrController(flickrActivity, mApiService, mPhotoAdapter);

        PhotoPayload photoPayload = new PhotoPayload();
        when(mApiService.listObjects(anyString(),anyInt())).thenReturn(just(photoPayload));
    }

    @Test
    public void observingUserInput_whenQueryIsSubmited_shouldCallListOfPhotos() {
        mFlickrController.observingUserInput(mSearchView);

        mSearchView.setQuery("Dog", true);

        verify(mApiService).listObjects(eq("Dog"), anyInt());
    }

    @Test
    public void observingUserInput_whenQueryIsNotSubmitted_shouldNotCallListOfPhotos() {
        mFlickrController.observingUserInput(mSearchView);

        mSearchView.setQuery("Dog", false);

        verify(mApiService, never()).listObjects(anyString(), anyInt());
    }

    @Test
    public void observingUserInput_whenApiServiceThrowsError_shouldNotCallListOfPhotos() {
        when(mApiService.listObjects(anyString(),anyInt()))
                .thenReturn(Observable.error(new Exception("Error")));

        mFlickrController.observingUserInput(mSearchView);

        mSearchView.setQuery("Dog", true);

        verify(mApiService).listObjects(anyString(), anyInt());
        ShadowToast.showedToast(ERROR_TEXT);
    }
}