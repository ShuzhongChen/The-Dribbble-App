package com.shuzhongchen.ecdribbbo.view.bucket_list;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.shuzhongchen.ecdribbbo.R;
import com.shuzhongchen.ecdribbbo.view.base.SingleFragmentActivity;

import java.util.ArrayList;

/**
 * Created by shuzhongchen on 8/18/17.
 */

public class ChooseBucketActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected Fragment newFragment() {
        ArrayList<String> chosenBucketIds = getIntent().getStringArrayListExtra(
                BucketListFragment.KEY_CHOSEN_BUCKET_IDS);
        return BucketListFragment.newInstance(true, chosenBucketIds);
    }

    @NonNull
    @Override
    protected String getActivityTitle() {
        return getString(R.string.choose_bucket);
    }
}