package com.image.finder.models;

import io.reactivex.annotations.Nullable;

@SuppressWarnings("unused")
public class PhotoPayload {

    private Photos photos;

    private String stat;

    @Nullable
    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}


