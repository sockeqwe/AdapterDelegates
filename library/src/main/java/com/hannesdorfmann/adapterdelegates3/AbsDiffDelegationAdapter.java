package com.hannesdorfmann.adapterdelegates3;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * An implementation of an Adapter that already uses a {@link AdapterDelegatesManager} pretty same as
 * {@link AbsDelegationAdapter} but also uses {@link AsyncListDiffer} from support library 27.0.1 for
 * calculating diffs between old and new collections of items and doing it on background thread.
 * That's means that now you should not carry about {@link RecyclerView.Adapter#notifyItemChanged(int)}
 * and other methods of adapter, all that you need - submit a new list for adapter, and all diffs will be
 * calculated for you.
 * You just have to add the {@link AdapterDelegate}s i.e. in the constructor of a subclass that inheritance from this
 * class:
 * <pre>
 * {@code
 *    class MyAdapter extends AbsDiffDelegationAdapter<MyDataSourceType>{
 *        public MyAdapter(){
 *            this.delegatesManager.add(new FooAdapterDelegate());
 *            this.delegatesManager.add(new BarAdapterDelegate());
 *        }
 *    }
 * }
 * </pre>
 *
 * @param <T> The type of the item, must implement {@link DiffItem}
 * @author Sergey Opivalov
 */

public abstract class AbsDiffDelegationAdapter<T extends DiffItem> extends RecyclerView.Adapter {

    protected final AdapterDelegatesManager<List<T>> delegatesManager;
    private final AsyncListDiffer<T> differ;

    public AbsDiffDelegationAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        this.differ = new AsyncListDiffer<>(this, diffCallback);
        this.delegatesManager = new AdapterDelegatesManager<>();
    }

    public AbsDiffDelegationAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback,
                                    @NonNull AdapterDelegatesManager<List<T>> delegatesManager) {
        this.differ = new AsyncListDiffer<>(this, diffCallback);
        this.delegatesManager = delegatesManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(differ.getCurrentList(), position, holder, null);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        delegatesManager.onBindViewHolder(differ.getCurrentList(), position, holder, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(differ.getCurrentList(), position);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        delegatesManager.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return delegatesManager.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        delegatesManager.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        delegatesManager.onViewDetachedFromWindow(holder);
    }

    /**
     * Get the items / data source of this adapter
     *
     * @return The items / data source
     */
    public List<T> getItems() {
        return differ.getCurrentList();
    }

    /**
     * Set the items / data source of this adapter
     *
     * @param items The items / data source
     */
    public void setItems(List<T> items) {
        differ.submitList(items);
    }
}
