package com.image.finder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.image.finder.models.Photo;
import com.squareup.picasso.Picasso;

class PhotoHolder extends RecyclerView.ViewHolder {
    private ImageView mImageView;

    PhotoHolder(View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.item_image_view);
    }

    void bind(Photo photo) {
        String imagePath = "http://farm"
                + Integer.toString(photo.getFarm())
                + ".static.flickr.com/"
                + photo.getServer() + "/" + photo.getId() + "_"
                + photo.getSecret() + ".jpg";

        Picasso.get().load(imagePath).into(mImageView);
    }
}
