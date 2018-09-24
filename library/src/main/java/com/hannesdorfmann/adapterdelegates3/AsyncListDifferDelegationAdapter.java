package com.hannesdorfmann.adapterdelegates3;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * An implementation of an Adapter that already uses a {@link AdapterDelegatesManager} pretty same as
 * {@link AbsDelegationAdapter} but also uses {@link AsyncListDiffer} from support library 27.0.1 for
 * calculating diffs between old and new collections of items and does this on background thread.
 * That means that now you should not carry about {@link RecyclerView.Adapter#notifyItemChanged(int)}
 * and other methods of adapter, all you need to do is to submit a new list into adapter and all diffs will be
 * calculated for you.
 * You just have to add the {@link AdapterDelegate}s i.e. in the constructor of a subclass that inheritance from this
 * class:
 * <pre>
 * {@code
 *    class MyAdapter extends AsyncListDifferDelegationAdapter<MyDataSourceType> {
 *        public MyAdapter() {
 *            this.delegatesManager.add(new FooAdapterDelegate())
 *                                 .add(new BarAdapterDelegate());
 *        }
 *    }
 * }
 * </pre>
 *
 * @param <T> The type of the datasource / items. Internally we will use List&lt;T&gt; but you only have
 *            to provide T (and not List&lt;T&gt;). Its safe to use this with
 *            {@link AbsListItemAdapterDelegate}.
 * @author Sergey Opivalov
 * @author Hannes Dorfmann
 */

public class AsyncListDifferDelegationAdapter<T> extends RecyclerView.Adapter {

    protected final AdapterDelegatesManager<List<T>> delegatesManager;
    protected final AsyncListDiffer<T> differ;

    public AsyncListDifferDelegationAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        this(diffCallback, new AdapterDelegatesManager<List<T>>());
    }

    public AsyncListDifferDelegationAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback,
                                            @NonNull AdapterDelegatesManager<List<T>> delegatesManager) {

        if (diffCallback == null) {
            throw new NullPointerException("ItemCallback is null");
        }

        if (delegatesManager == null) {
            throw new NullPointerException("AdapterDelegatesManager is null");
        }
        this.differ = new AsyncListDiffer<T>(this, diffCallback);
        this.delegatesManager = delegatesManager;
    }

    public AsyncListDifferDelegationAdapter(@NonNull AsyncDifferConfig differConfig,
                                            @NonNull AdapterDelegatesManager<List<T>> delegatesManager) {

        if (differConfig == null) {
            throw new NullPointerException("AsyncDifferConfig is null");
        }

        if (delegatesManager == null) {
            throw new NullPointerException("AdapterDelegatesManager is null");
        }

        this.differ = new AsyncListDiffer<T>(new AdapterListUpdateCallback(this), differConfig);
        this.delegatesManager = delegatesManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(differ.getCurrentList(), position, holder, null);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        delegatesManager.onBindViewHolder(differ.getCurrentList(), position, holder, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(differ.getCurrentList(), position);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        delegatesManager.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        return delegatesManager.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        delegatesManager.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
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


    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }
}
