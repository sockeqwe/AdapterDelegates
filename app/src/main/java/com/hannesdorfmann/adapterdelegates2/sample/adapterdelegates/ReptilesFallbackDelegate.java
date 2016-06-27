package com.hannesdorfmann.adapterdelegates2.sample.adapterdelegates;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hannesdorfmann.adapterdelegates2.AbsFallbackAdapterDelegate;
import com.hannesdorfmann.adapterdelegates2.sample.R;

/**
 * @author Hannes Dorfmann
 */
public class ReptilesFallbackDelegate extends AbsFallbackAdapterDelegate {

  private LayoutInflater inflater;

  public ReptilesFallbackDelegate(Activity activity) {
    inflater = activity.getLayoutInflater();
  }

  @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
    View view = inflater.inflate(R.layout.item_unknown_reptile, parent, false);
    return new ReptileFallbackViewHolder(view);
  }

  @Override public void onBindViewHolder(@NonNull Object items, int position,
      @NonNull RecyclerView.ViewHolder holder) {

  }

  @Override
  public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
    Log.d("AdapterDelegates", "ViewHolder got recycled.");
  }

  @Override
  public boolean onFailedToRecycleView(RecyclerView.ViewHolder viewHolder) {
    Log.w("AdapterDelegates", "Failed to recycle a ViewHolder.");
    return false;
  }

  class ReptileFallbackViewHolder extends RecyclerView.ViewHolder {
    public ReptileFallbackViewHolder(View itemView) {
      super(itemView);
    }
  }
}
