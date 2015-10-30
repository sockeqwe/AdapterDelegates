package com.hannesdorfmann.adapterdelegates.sample.adapterdelegates;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hannesdorfmann.adapterdelegates.AbsFallbackAdapterDelegate;
import com.hannesdorfmann.adapterdelegates.sample.R;

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

  class ReptileFallbackViewHolder extends RecyclerView.ViewHolder {
    public ReptileFallbackViewHolder(View itemView) {
      super(itemView);
    }
  }
}
