package com.hannesdorfmann.adapterdelegates4.paging;

import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate;
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A {@link PagedListAdapter} that uses {@link AdapterDelegatesManager}
 * and {@link com.hannesdorfmann.adapterdelegates4.AdapterDelegate}
 *
 * @param <T> The type of {@link PagedList}
 */
public class PagedListDelegationAdapter<T> extends PagedListAdapter<T, RecyclerView.ViewHolder> {

    protected final AdapterDelegatesManager<List<T>> delegatesManager;

    /**
     * @param diffCallback The Callback
     * @param delegates    The {@link AdapterDelegate}s that should be added
     * @since 4.1.0
     */
    public PagedListDelegationAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback, AdapterDelegate<List<T>>... delegates) {
        this(new AdapterDelegatesManager<List<T>>(), diffCallback);
        for (int i = 0; i < delegates.length; i++) {
            delegatesManager.addDelegate(delegates[i]);
        }
    }

    public PagedListDelegationAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        this(new AdapterDelegatesManager<List<T>>(), diffCallback);
    }

    public PagedListDelegationAdapter(@NonNull AdapterDelegatesManager<List<T>> delegatesManager,
                                      @NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
        if (diffCallback == null) {
            throw new NullPointerException("ItemCallback is null");
        }

        if (delegatesManager == null) {
            throw new NullPointerException("AdapterDelegatesManager is null");
        }
        this.delegatesManager = delegatesManager;
    }

    public PagedListDelegationAdapter(@NonNull AsyncDifferConfig<T> config) {
        this(new AdapterDelegatesManager<List<T>>(), config);
    }

    public PagedListDelegationAdapter(@NonNull AdapterDelegatesManager<List<T>> delegatesManager,
                                      @NonNull AsyncDifferConfig<T> config) {
        super(config);
        if (config == null) {
            throw new NullPointerException("AsyncDifferConfig is null");
        }
        if (delegatesManager == null) {
            throw new NullPointerException("AdapterDelegatesManager is null");
        }
        this.delegatesManager = delegatesManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        getItem(position); // Internally triggers loading items around items around the given position
        delegatesManager.onBindViewHolder(getCurrentList(), position, holder, null);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position,
                                 @NonNull List payloads) {
        getItem(position); // Internally triggers loading items around items around the given position
        delegatesManager.onBindViewHolder(getCurrentList(), position, holder, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(getCurrentList(), position);
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
}
