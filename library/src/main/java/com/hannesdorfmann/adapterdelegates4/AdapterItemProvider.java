package com.hannesdorfmann.adapterdelegates4;

import androidx.annotation.Nullable;

public interface AdapterItemProvider<T> {

    @Nullable
    T getAdapterItem(int position);

    int getAdapterItemCount();
}
