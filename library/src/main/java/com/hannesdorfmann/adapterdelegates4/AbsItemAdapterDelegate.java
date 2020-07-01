package com.hannesdorfmann.adapterdelegates4;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class AbsItemAdapterDelegate<I extends T, T, VH extends RecyclerView.ViewHolder>
        extends AdapterDelegate<AdapterItemProvider<T>> {

    @Override
    protected final boolean isForViewType(@NonNull AdapterItemProvider<T> items, int position) {
        return isForViewType(items.getAdapterItem(position), items, position);
    }

    @Override
    protected final void onBindViewHolder(@NonNull AdapterItemProvider<T> items, int position,
                                          @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        onBindViewHolder((I) items.getAdapterItem(position), (VH) holder, payloads);
    }

    protected abstract boolean isForViewType(@NonNull T item, @NonNull AdapterItemProvider<T> items, int position);

    @NonNull
    @Override
    protected abstract VH onCreateViewHolder(@NonNull ViewGroup parent);

    protected abstract void onBindViewHolder(@NonNull I item, @NonNull VH holder,
                                             @NonNull List<Object> payloads);
}

