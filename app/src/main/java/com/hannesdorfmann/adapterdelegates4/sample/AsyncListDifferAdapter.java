package com.hannesdorfmann.adapterdelegates4.sample;


import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter;
import com.hannesdorfmann.adapterdelegates4.sample.adapterdelegates.DiffCatAdapterDelegate;
import com.hannesdorfmann.adapterdelegates4.sample.adapterdelegates.DiffDogAdapterDelegate;
import com.hannesdorfmann.adapterdelegates4.sample.model.DiffItem;

import androidx.recyclerview.widget.DiffUtil;

/**
 * @author Sergey Opivalov
 */
public class AsyncListDifferAdapter extends AsyncListDifferDelegationAdapter<DiffItem> {

    public AsyncListDifferAdapter() {
        super(DIFF_CALLBACK);
        delegatesManager
                .addDelegate(new DiffDogAdapterDelegate())
                .addDelegate(new DiffCatAdapterDelegate());
    }

    private static final DiffUtil.ItemCallback<DiffItem> DIFF_CALLBACK
            = new DiffUtil.ItemCallback<DiffItem>() {

        @Override
        public boolean areItemsTheSame(DiffItem oldItem, DiffItem newItem) {
            return oldItem.getItemId() == newItem.getItemId();
        }

        @Override
        public boolean areContentsTheSame(DiffItem oldItem, DiffItem newItem) {
            return oldItem.getItemHash() == newItem.getItemHash();
        }
    };
}
