package com.shuzhongchen.ecdribbbo.view.bucket_list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuzhongchen.ecdribbbo.R;
import com.shuzhongchen.ecdribbbo.view.base.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by shuzhongchen on 7/26/17.
 */

public class BucketViewHolder extends BaseViewHolder {

    @BindView(R.id.bucket_layout) View bucketLayout;
    @BindView(R.id.bucket_name) TextView bucketName;
    @BindView(R.id.bucket_shot_count) TextView bucketShotCount;
    @BindView(R.id.bucket_shot_chosen) ImageView bucketChosen;

    public BucketViewHolder(View itemView) {
        super(itemView);
    }
}
