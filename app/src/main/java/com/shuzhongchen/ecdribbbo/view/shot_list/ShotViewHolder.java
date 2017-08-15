package com.shuzhongchen.ecdribbbo.view.shot_list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shuzhongchen.ecdribbbo.R;
import com.shuzhongchen.ecdribbbo.view.base.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by shuzhongchen on 7/25/17.
 */

public class ShotViewHolder extends BaseViewHolder {

    @BindView(R.id.shot_view_count) public TextView viewCount;
    @BindView(R.id.shot_like_count) public TextView likeCount;
    @BindView(R.id.shot_bucket_count) public TextView bucketCount;
    @BindView(R.id.shot_image) SimpleDraweeView image;
    @BindView(R.id.shot_clickable_cover) public View cover;

    public ShotViewHolder(View itemView) {super(itemView);}
}
