package com.hannesdorfmann.adapterdelegates3;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Similar with {@link ListDelegationAdapter} it's implementation designed for items organized in a {@link List}.
 * This adapter implementation is ready to go. All you have to do is to add {@link AdapterDelegate}s to the
 * internal {@link AdapterDelegatesManager} i.e. in the constructor:
 * <p>
 * <pre>
 * {@code
 *    class MyAdapter extends DiffDelegationAdapter<List<Foo>>{
 *        public MyAdapter(){
 *            this.delegatesManager.add(new FooAdapterDelegate());
 *            this.delegatesManager.add(new BarAdapterDelegate());
 *        }
 *    }
 * }
 * </pre>
 *
 * @param <T> The type of the items. Must implement {@link DiffItem}
 * @author Sergey Opivalov
 */

public class DiffDelegationAdapter<T extends DiffItem> extends AbsDiffDelegationAdapter<T> {

    public DiffDelegationAdapter() {
        super(new DiffCallbackImpl<T>());
    }

    public DiffDelegationAdapter(@NonNull DiffUtil.ItemCallback<T> itemCallback) {
        super(itemCallback);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }
}
