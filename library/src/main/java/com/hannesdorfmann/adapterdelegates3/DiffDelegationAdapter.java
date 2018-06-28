package com.hannesdorfmann.adapterdelegates3;

/**
 * Created by six_hundreds on 15.06.18.
 */

public class DiffDelegationAdapter<T extends DiffItem> extends AbsDiffDelegationAdapter<T> {

    public DiffDelegationAdapter() {
        super(new DiffCallbackImpl<T>());
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }
}
