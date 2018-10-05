package com.hannesdorfmann.adapterdelegates4.sample.animations;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate;
import com.hannesdorfmann.adapterdelegates4.sample.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    @NonNull
    @Override
    protected ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new ItemViewHolder(inflater.inflate(R.layout.item_animation, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull Item item, @NonNull ItemViewHolder holder,
                                    @NonNull List<Object> payloads) {

        Log.d("ItemAdapterDelegate", "Change Payload: " + payloads);

        holder.textView.setText(item.text);
        holder.textView.setBackgroundColor(item.color);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public ItemViewHolder(View v) {
            super(v);
            this.textView = (TextView) v;
        }
    }
}
