package com.hannesdorfmann.adapterdelegates3;

import android.support.v7.util.DiffUtil;

/**
 * Implementation of {@link DiffUtil.ItemCallback} that ready to go. It used in
 * {@link DiffDelegationAdapter} for reducing boilerplate and in most cases all you need to do
 * is to provide type of your items as parameter.
 *
 * @param <T> The type of the items
 * @author Sergey Opivalov
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
