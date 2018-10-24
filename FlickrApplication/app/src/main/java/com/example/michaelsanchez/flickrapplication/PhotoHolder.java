package com.example.michaelsanchez.flickrapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.michaelsanchez.flickrapplication.Photos.Photo;
import com.squareup.picasso.Picasso;

public class PhotoHolder extends RecyclerView.ViewHolder {
    private ImageView mImageView;
    private Context mContext;

    public PhotoHolder(View itemView, Context context) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.item_image_view);
        mContext = context;
    }

    public void loadImage(Photo photo) {
        String imagePath;

        imagePath = "http://farm"
                + Integer.toString(photo.getFarm())
                + ".static.flickr.com/"
                + photo.getServer() + "/" + photo.getId() + "_"
                + photo.getSecret() + ".jpg";

        Picasso.get().load(imagePath).into(mImageView);
    }
}
