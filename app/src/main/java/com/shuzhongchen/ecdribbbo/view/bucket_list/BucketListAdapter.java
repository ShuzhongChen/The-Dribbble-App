package com.shuzhongchen.ecdribbbo.view.bucket_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuzhongchen.ecdribbbo.R;
import com.shuzhongchen.ecdribbbo.model.Bucket;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuzhongchen on 7/26/17.
 */

public class BucketListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_BUCKET = 1;
    private static final int VIEW_TYPE_LOADING = 2;

    private List<Bucket> data;
    private LoadMoreListener loadMoreListener;
    private boolean isChoosingMode;
    private boolean showLoading;

    public BucketListAdapter(@NonNull List<Bucket> data,
                             @NonNull LoadMoreListener loadMoreListener,
                             boolean isChoosingMode) {
        this.data = data;
        this.loadMoreListener = loadMoreListener;
        this.isChoosingMode = isChoosingMode;
        this.showLoading = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_BUCKET) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_bucket, parent, false);
            return new BucketViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_loading, parent, false);
            return new RecyclerView.ViewHolder(view) {};
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_LOADING) {
            loadMoreListener.onLoadMore();
        } else {
            final Bucket bucket = data.get(position);

            Context context = holder.itemView.getContext();

            // 0 -> 0 shot
            // 1 -> 1 shot
            // 2 -> 2 shots
            String bucketShotCountString = MessageFormat.format(
                    holder.itemView.getContext().getResources().getString(R.string.shot_count),
                    bucket.shots_count);

            BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;
            bucketViewHolder.bucketName.setText(bucket.name);
            bucketViewHolder.bucketShotCount.setText(bucketShotCountString);

            if (isChoosingMode) {
                bucketViewHolder.bucketChosen.setVisibility(View.VISIBLE);
                bucketViewHolder.bucketChosen.setImageDrawable(
                        bucket.isChoosing
                                ? ContextCompat.getDrawable(context, R.drawable.ic_check_box_black_24dp)
                                : ContextCompat.getDrawable(context, R.drawable.ic_check_box_outline_blank_black_24dp));
                bucketViewHolder.bucketLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bucket.isChoosing = !bucket.isChoosing;
                        notifyItemChanged(position);
                    }
                });
            } else {
                bucketViewHolder.bucketChosen.setVisibility(View.GONE);
                bucketViewHolder.bucketLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // if not in choosing mode, we need to open a new Activity to show
                        // what shots are in this bucket, we will need ShotListFragment here!
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return showLoading ? data.size() + 1 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position < data.size()
                ? VIEW_TYPE_BUCKET
                : VIEW_TYPE_LOADING;
    }

    public void append(@NonNull List<Bucket> moreBuckets) {
        data.addAll(moreBuckets);
        notifyDataSetChanged();
    }

    public void prepend(@NonNull List<Bucket> data) {
        this.data.addAll(0, data);
        notifyDataSetChanged();
    }

    public int getDataCount() {
        return data.size();
    }

    @NonNull
    public ArrayList<String> getSelectedBucketIds() {
        ArrayList<String> selectedBucketIds = new ArrayList<>();
        for (Bucket bucket : data) {
            if (bucket.isChoosing) {
                selectedBucketIds.add(bucket.id);
            }
        }
        return selectedBucketIds;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }
}
