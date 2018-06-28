package com.hannesdorfmann.adapterdelegates3;

import android.support.v7.util.DiffUtil;

/**
 * Created by six_hundreds on 15.06.18.
 */

public class DiffCallbackImpl<T extends DiffItem> extends DiffUtil.ItemCallback<T> {

    @Override
    public boolean areItemsTheSame(T oldItem, T newItem) {
        return oldItem.getItemId() == newItem.getItemId();
    }

    @Override
    public boolean areContentsTheSame(T oldItem, T newItem) {
        return oldItem.getItemHash() == newItem.getItemHash();
    }
}
