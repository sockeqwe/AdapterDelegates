package com.hannesdorfmann.adapterdelegates4.sample.pagination;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates4.AbsFallbackAdapterDelegate;
import com.hannesdorfmann.adapterdelegates4.sample.R;
import com.hannesdorfmann.adapterdelegates4.sample.model.DisplayableItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LoadingAdapterDelegate extends AbsFallbackAdapterDelegate<List<DisplayableItem>> {

    private final LayoutInflater inflater;

    public LoadingAdapterDelegate(Activity activity) {
        this.inflater = activity.getLayoutInflater();
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new LoadingViewHolder(inflater.inflate(R.layout.loading, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<DisplayableItem> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        Log.d("Tes", "test");
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
