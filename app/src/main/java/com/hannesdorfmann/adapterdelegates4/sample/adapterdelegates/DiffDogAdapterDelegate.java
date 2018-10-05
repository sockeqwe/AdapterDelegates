package com.hannesdorfmann.adapterdelegates4.sample.adapterdelegates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate;
import com.hannesdorfmann.adapterdelegates4.sample.R;
import com.hannesdorfmann.adapterdelegates4.sample.model.DiffDog;
import com.hannesdorfmann.adapterdelegates4.sample.model.DiffItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Sergey Opivalov
 */
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
