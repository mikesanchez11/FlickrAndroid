package com.image.finder;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class FlickrScrollListener extends RecyclerView.OnScrollListener {
    private GridLayoutManager mGridLayourManager;
    private FlickrController mController;

    FlickrScrollListener(GridLayoutManager gridLayoutManager, FlickrController controller) {
        mGridLayourManager = gridLayoutManager;
        mController = controller;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int mVisibleItemCount = mGridLayourManager.getChildCount();
        int mTotalItemCount = mGridLayourManager.getItemCount();
        int mFirstVisibleItemPosition = mGridLayourManager.findFirstVisibleItemPosition();

        if (mVisibleItemCount + mFirstVisibleItemPosition >= mTotalItemCount) { // amount of items that are being shown plus the previous items displayed
            mController.handleScroll(); // check here, handle scroll is being called multiple times issue is either here or in the actuall handle scroll method
        }
    }
}