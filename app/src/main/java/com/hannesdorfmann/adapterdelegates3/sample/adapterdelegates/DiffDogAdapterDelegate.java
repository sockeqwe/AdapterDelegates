package com.hannesdorfmann.adapterdelegates3.sample.adapterdelegates;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates.sample.R;
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate;
import com.hannesdorfmann.adapterdelegates3.DiffItem;
import com.hannesdorfmann.adapterdelegates3.sample.model.DiffDog;

import java.util.List;

public class DiffDogAdapterDelegate extends AbsListItemAdapterDelegate<DiffDog, DiffItem, DiffDogAdapterDelegate.ViewHolder> {

    @Override
    protected boolean isForViewType(@NonNull DiffItem item,
                                    @NonNull List<DiffItem> items,
                                    int position) {
        return item instanceof DiffDog;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diff_dog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull DiffDog item,
                                    @NonNull ViewHolder holder,
                                    @NonNull List<Object> payloads) {
        holder.bind(item);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;

        private TextView age;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.age);
        }

        void bind(DiffDog item) {
            name.setText(item.getName());
            age.setText(String.valueOf(item.getAge()));
        }
    }
}
