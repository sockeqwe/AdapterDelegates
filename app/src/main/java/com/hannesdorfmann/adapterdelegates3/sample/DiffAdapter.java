package com.hannesdorfmann.adapterdelegates3.sample;

import com.hannesdorfmann.adapterdelegates3.DiffDelegationAdapter;
import com.hannesdorfmann.adapterdelegates3.sample.adapterdelegates.DiffCatAdapterDelegate;
import com.hannesdorfmann.adapterdelegates3.sample.adapterdelegates.DiffDogAdapterDelegate;

public class DiffAdapter extends DiffDelegationAdapter<DiffItem> {

    public DiffAdapter() {
        delegatesManager.addDelegate(new DiffDogAdapterDelegate());
        delegatesManager.addDelegate(new DiffCatAdapterDelegate());
    }
}
