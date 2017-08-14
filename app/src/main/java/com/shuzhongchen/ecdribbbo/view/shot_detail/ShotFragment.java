package com.shuzhongchen.ecdribbbo.view.shot_detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.shuzhongchen.ecdribbbo.R;
import com.shuzhongchen.ecdribbbo.model.Shot;
import com.shuzhongchen.ecdribbbo.utils.ModelUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shuzhongchen on 8/2/17.
 */

public class ShotFragment extends Fragment {

    public static final String KEY_SHOT = "shot";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static ShotFragment newInstance(@NonNull Bundle args) {
        ShotFragment fragment = new ShotFragment();
        fragment.setArguments(args);
        return fragment;
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
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        Shot shot = ModelUtils.toObject(getArguments().getString(KEY_SHOT),
                new TypeToken<Shot>(){});
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ShotAdapter(shot));
    }
}