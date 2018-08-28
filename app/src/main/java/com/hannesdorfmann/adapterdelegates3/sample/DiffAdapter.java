package com.hannesdorfmann.adapterdelegates3.sample;

import android.support.v7.util.DiffUtil;

import com.hannesdorfmann.adapterdelegates3.DiffDelegationAdapter;
import com.hannesdorfmann.adapterdelegates3.sample.adapterdelegates.DiffCatAdapterDelegate;
import com.hannesdorfmann.adapterdelegates3.sample.adapterdelegates.DiffDogAdapterDelegate;
import com.hannesdorfmann.adapterdelegates3.sample.model.DiffItem;

/**
 * @author Sergey Opivalov
 */

public class DiffAdapter extends DiffDelegationAdapter<DiffItem> {

    public DiffAdapter() {
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
