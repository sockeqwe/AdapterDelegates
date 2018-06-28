package com.hannesdorfmann.adapterdelegates3;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by six_hundreds on 13.06.18.
 */

public abstract class AbsDiffDelegationAdapter<T extends DiffItem> extends RecyclerView.Adapter {

    protected final AdapterDelegatesManager<List<T>> delegatesManager;
    protected final AsyncListDiffer<T> differ;

    public AbsDiffDelegationAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        this.differ = new AsyncListDiffer<>(this, diffCallback);
        this.delegatesManager = new AdapterDelegatesManager<>();
    }

    public AbsDiffDelegationAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback,
                                    @NonNull AdapterDelegatesManager<List<T>> delegatesManager) {
        this.differ = new AsyncListDiffer<>(this, diffCallback);
        this.delegatesManager = delegatesManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(differ.getCurrentList(), position, holder, null);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        delegatesManager.onBindViewHolder(differ.getCurrentList(), position, holder, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(differ.getCurrentList(), position);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        delegatesManager.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return delegatesManager.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        delegatesManager.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        delegatesManager.onViewDetachedFromWindow(holder);
    }

    /**
     * Get the items / data source of this adapter
     *
     * @return The items / data source
     */
    public List<T> getItems() {
        return differ.getCurrentList();
    }

    /**
     * Set the items / data source of this adapter
     *
     * @param items The items / data source
     */
    public void setItems(List<T> items) {
        differ.submitList(items);
    }
}
