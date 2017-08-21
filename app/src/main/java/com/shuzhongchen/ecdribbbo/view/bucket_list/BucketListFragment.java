package com.shuzhongchen.ecdribbbo.view.bucket_list;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonSyntaxException;
import com.shuzhongchen.ecdribbbo.R;
import com.shuzhongchen.ecdribbbo.dribbble.Dribbble;
import com.shuzhongchen.ecdribbbo.model.Bucket;
import com.shuzhongchen.ecdribbbo.view.base.SpaceItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shuzhongchen on 7/26/17.
 */

public class BucketListFragment extends Fragment {

    public static final int REQ_CODE_NEW_BUCKET = 100;
    public static final String KEY_CHOOSING_MODE = "choose_mode";
    public static final String KEY_CHOSEN_BUCKET_IDS = "chosen_bucket_ids";

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.fab) FloatingActionButton fab;

    private BucketListAdapter adapter;
    private boolean isChoosingMode;
    private List<String> chosenBucketIds;

    public static BucketListFragment newInstance(boolean isChoosingMode,
                                                 @Nullable ArrayList<String> chosenBucketIds) {
        Bundle args = new Bundle();
        args.putBoolean(KEY_CHOOSING_MODE, isChoosingMode);
        args.putStringArrayList(KEY_CHOSEN_BUCKET_IDS, chosenBucketIds);

        BucketListFragment fragment = new BucketListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fab_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isChoosingMode = getArguments().getBoolean(KEY_CHOOSING_MODE);
        if (isChoosingMode) {
            chosenBucketIds = getArguments().getStringArrayList(KEY_CHOSEN_BUCKET_IDS);
            if (chosenBucketIds == null) {
                chosenBucketIds = new ArrayList<>();
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.spacing_medium)));

        adapter = new BucketListAdapter(new ArrayList<Bucket>(), new BucketListAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                AsyncTaskCompat.executeParallel(
                        new LoadBucketTask(adapter.getDataCount() / Dribbble.COUNT_PER_PAGE + 1));
            }
        }, isChoosingMode);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewBucketDialogFragment dialogFragment = NewBucketDialogFragment.newInstance();
                dialogFragment.setTargetFragment(BucketListFragment.this, REQ_CODE_NEW_BUCKET);
                dialogFragment.show(getFragmentManager(), NewBucketDialogFragment.TAG);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_NEW_BUCKET && resultCode == Activity.RESULT_OK) {
            String bucketName = data.getStringExtra(NewBucketDialogFragment.KEY_BUCKET_NAME);
            String bucketDescription = data.getStringExtra(NewBucketDialogFragment.KEY_BUCKET_DESCRIPTION);
            if (!TextUtils.isEmpty(bucketName)) {
                AsyncTaskCompat.executeParallel(new NewBucketTask(bucketName, bucketDescription));
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isChoosingMode) {
            inflater.inflate(R.menu.bucket_list_choose_mode_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            ArrayList<String> chosenBucketIds = adapter.getSelectedBucketIds();

            Intent result = new Intent();
            result.putStringArrayListExtra(KEY_CHOSEN_BUCKET_IDS, chosenBucketIds);
            getActivity().setResult(Activity.RESULT_OK, result);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class LoadBucketTask extends AsyncTask<Void, Void, List<Bucket>> {

        int page;

        public LoadBucketTask(int page) {
            this.page = page;
        }

        @Override
        protected List<Bucket> doInBackground(Void... params) {
            // this method is executed on non-UI thread
            try {
                return Dribbble.getUserBuckets(page);
            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Bucket> buckets) {
            // this method is executed on UI thread!!!!
            if (buckets != null) {
                if (isChoosingMode) {
                    // mark each bucket whether it's been chosen
                    for (Bucket bucket : buckets) {
                        if (chosenBucketIds.contains(bucket.id)) {
                            bucket.isChoosing = true;
                        }
                    }
                }

                adapter.append(buckets);
                adapter.setShowLoading(buckets.size() == Dribbble.COUNT_PER_PAGE);
            } else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private class NewBucketTask extends AsyncTask<Void, Void, Bucket> {

        private String name;
        private String description;

        private NewBucketTask(String name, String description) {
            this.name = name;
            this.description = description;
        }

        @Override
        protected Bucket doInBackground(Void... params) {
            try {
                return Dribbble.newBucket(name, description);
            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bucket newBucket) {
            // this method is executed on UI thread!!!!
            if (newBucket != null) {
                adapter.prepend(Collections.singletonList(newBucket));
            } else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
