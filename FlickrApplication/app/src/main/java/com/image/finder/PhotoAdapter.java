package com.image.finder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.image.finder.models.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
    private List<Photo> mPhotoItems;

    PhotoAdapter() {
        mPhotoItems = new ArrayList<>();
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item_gallery, viewGroup, false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoHolder photoHolder, int position) {
        Photo photoItem = mPhotoItems.get(position);
        photoHolder.bind(photoItem);
    }

    @Override
    public int getItemCount() {
        return mPhotoItems.size();
    }

    void updatingPhotoAdapter(List<Photo> photoList) {
        mPhotoItems.addAll(photoList);
        notifyDataSetChanged();
    }

    public void clear() {
        mPhotoItems.clear();
        notifyDataSetChanged();
    }
}
