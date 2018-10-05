package com.hannesdorfmann.adapterdelegates4.sample.adapterdelegates;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate;
import com.hannesdorfmann.adapterdelegates4.sample.R;
import com.hannesdorfmann.adapterdelegates4.sample.model.DisplayableItem;
import com.hannesdorfmann.adapterdelegates4.sample.model.Snake;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Hannes Dorfmann
 */
public class SnakeListItemAdapterDelegate extends
        AbsListItemAdapterDelegate<Snake, DisplayableItem, SnakeListItemAdapterDelegate.SnakeViewHolder> {

    private LayoutInflater inflater;

    public SnakeListItemAdapterDelegate(Activity activity) {
        inflater = activity.getLayoutInflater();
    }

    @Override
    protected boolean isForViewType(@NonNull DisplayableItem item, @NonNull List<DisplayableItem> items,
                                    int position) {
        return item instanceof Snake;
    }

    @NonNull
    @Override
    public SnakeListItemAdapterDelegate.SnakeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent) {
        return new SnakeListItemAdapterDelegate.SnakeViewHolder(
                inflater.inflate(R.layout.item_snake, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull Snake snake,
                                    @NonNull SnakeListItemAdapterDelegate.SnakeViewHolder vh, @Nullable List payloads) {

        vh.name.setText(snake.getName());
        vh.race.setText(snake.getRace());
    }

    static class SnakeViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView race;

        public SnakeViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            race = (TextView) itemView.findViewById(R.id.race);
        }
    }
}
