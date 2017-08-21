package com.shuzhongchen.ecdribbbo.view.shot_list;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.shuzhongchen.ecdribbbo.R;
import com.shuzhongchen.ecdribbbo.dribbble.Dribbble;
import com.shuzhongchen.ecdribbbo.model.Shot;
import com.shuzhongchen.ecdribbbo.model.User;
import com.shuzhongchen.ecdribbbo.view.base.SpaceItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shuzhongchen on 7/25/17.
 */

public class ShotListFragment extends Fragment {

    public static final int REQ_CODE_SHOT = 100;
    public static final String KEY_LIST_TYPE = "listType";
    public static final String KEY_BUCKET_ID = "bucketId";

    public static final int LIST_TYPE_POPULAR = 1;
    public static final int LIST_TYPE_LIKED = 2;
    public static final int LIST_TYPE_BUCKET = 3;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private static final int COUNT_PER_PAGE = 20;

    private ShotListAdapter adapter;

    public static ShotListFragment newInstance() {
        return new ShotListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_medium)));

        final Handler handler = new Handler();
        adapter = new ShotListAdapter(new ArrayList<Shot>(), new ShotListAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                // this method will be called when the RecyclerView is displayed
                // page starts from 1
                AsyncTaskCompat.executeParallel(
                        new LoadShotTask(adapter.getDataCount() / Dribbble.COUNT_PER_PAGE + 1));
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private class LoadShotTask extends AsyncTask<Void, Void, List<Shot>> {

        int page;

        public LoadShotTask(int page) {
            this.page = page;
        }

        @Override
        protected List<Shot> doInBackground(Void... params) {
            // this method is executed on non-UI thread
            try {
                return Dribbble.getShots(page);
            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Shot> shots) {
            // this method is executed on UI thread!!!!
            if (shots != null) {
                adapter.append(shots);
                adapter.setShowLoading(shots.size() == Dribbble.COUNT_PER_PAGE);
            } else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
