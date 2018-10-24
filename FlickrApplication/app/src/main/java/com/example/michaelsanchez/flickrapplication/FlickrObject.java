package com.example.michaelsanchez.flickrapplication;

import com.example.michaelsanchez.flickrapplication.Photos.Photo;
import com.example.michaelsanchez.flickrapplication.Photos.Photos;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlickrObject {

    @SerializedName("photos")
    @Expose
    private Photos photos;
    @SerializedName("stat")
    @Expose
    private String stat;

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public FlickrObject(Photos photos, String stat) {
        this.photos = photos;
        this.stat = stat;
    }

    public FlickrObject() {
        photos = null;
        stat = null;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}


