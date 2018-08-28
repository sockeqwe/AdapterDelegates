package com.hannesdorfmann.adapterdelegates3;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Similar to {@link ListDelegationAdapter}, this implementation designed for items organized in a {@link List}.
 * This adapter implementation is ready to go. All you have to do is to add {@link AdapterDelegate}s to the
 * internal {@link AdapterDelegatesManager} i.e. in the constructor:
 * <p>
 * <pre>
 * {@code
 *    class MyAdapter extends DiffDelegationAdapter<List<Foo>> {
 *        public MyAdapter() {
 *            this.delegatesManager.add(new FooAdapterDelegate())
 *                                 .add(new BarAdapterDelegate());
 *        }
 *    }
 * }
 * </pre>
 *
 * @param <T> The type of the datasource / items
 * @author Sergey Opivalov
 */

public class DiffDelegationAdapter<T> extends AbsDiffDelegationAdapter<T> {

    public DiffDelegationAdapter(@NonNull DiffUtil.ItemCallback<T> itemCallback) {
        super(itemCallback);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }
}
