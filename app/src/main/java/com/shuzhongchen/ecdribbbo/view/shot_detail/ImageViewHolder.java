package com.shuzhongchen.ecdribbbo.view.shot_detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by shuzhongchen on 8/1/17.
 */

class ImageViewHolder extends RecyclerView.ViewHolder {

    SimpleDraweeView image;

    public ImageViewHolder(View itemView) {
        super(itemView);
        image = (SimpleDraweeView) itemView;
    }
}
