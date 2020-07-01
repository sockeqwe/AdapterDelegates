package com.hannesdorfmann.adapterdelegates4.paging3

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AdapterItemProvider

open class PagingDelegationAdapter<T : Any>(
    protected val delegatesManager: AdapterDelegatesManager<AdapterItemProvider<T>>,
    diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, RecyclerView.ViewHolder>(diffCallback = diffCallback),
    AdapterItemProvider<T> {

    constructor(diffCallback: DiffUtil.ItemCallback<T>) : this(
        AdapterDelegatesManager<AdapterItemProvider<T>>(),
        diffCallback
    )

    constructor(
        diffCallback: DiffUtil.ItemCallback<T>,
        vararg delegates: AdapterDelegate<AdapterItemProvider<T>>
    ) : this(
        AdapterDelegatesManager<AdapterItemProvider<T>>().apply {
            delegates.forEach { this.addDelegate(it) }
        },
        diffCallback
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(this, position, holder, null)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        delegatesManager.onBindViewHolder(this, position, holder, payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(this, position)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return delegatesManager.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewDetachedFromWindow(holder)
    }

    override fun getAdapterItem(position: Int): T? {
        return getItem(position)
    }

    override fun getAdapterItemCount(): Int {
        return itemCount
    }
}