package com.hannesdorfmann.adapterdelegates3.sample.animations;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hannesdorfmann.adapterdelegates.sample.R;
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */

public class ItemAdapterDelegate
    extends AbsListItemAdapterDelegate<Item, Item, ItemAdapterDelegate.ItemViewHolder> {

  private LayoutInflater inflater;

  public ItemAdapterDelegate(LayoutInflater inflater) {
    this.inflater = inflater;
  }

  @Override
  protected boolean isForViewType(@NonNull Item item, @NonNull List<Item> items, int position) {
    return true;
  }

  @NonNull @Override protected ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
    return new ItemViewHolder(inflater.inflate(R.layout.item_animation, parent, false));
  }

  @Override protected void onBindViewHolder(@NonNull Item item, @NonNull ItemViewHolder viewHolder,
      @NonNull List<Object> payloads) {

    Log.d("ItemAdapterDelegate", "Change Payload: " + payloads);

    viewHolder.textView.setText(item.text);
    viewHolder.textView.setBackgroundColor(item.color);
  }

  class ItemViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    public ItemViewHolder(View v) {
      super(v);
      this.textView = (TextView) v;
    }
  }
}
