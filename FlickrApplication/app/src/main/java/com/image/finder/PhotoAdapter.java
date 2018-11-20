package com.image.finder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.image.finder.Models.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
    private List<Photo> mPhotoItems;
    private Context mContext;

    public PhotoAdapter() {
        mPhotoItems = null;
        mContext = null;
    }
    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_gallery, viewGroup, false);
        return new PhotoHolder(view);
    }
    @Override
    public void onBindViewHolder(PhotoHolder photoHolder, int position) {
        Photo photoItem = mPhotoItems.get(position);
        photoHolder.loadImage(photoItem);
    }
    @Override
    public int getItemCount() {
        return mPhotoItems.size();
    }

    public void addMoreItems(List<Photo> photoList) {
        mPhotoItems.addAll(photoList);
        notifyDataSetChanged();
    }

    public void updatingPhotoAdapter(List<Photo> galleryItems, Context context) {
        mPhotoItems = galleryItems;
        mContext = context;
    }
}
