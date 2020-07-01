package com.hannesdorfmann.adapterdelegates4.dsl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterItemProvider

inline fun <reified I : T, T> adapterLayoutContainer(
    @LayoutRes layout: Int,
    noinline on: (item: T, items: AdapterItemProvider<T>, position: Int) -> Boolean = { item, _, _ -> item is I },
    noinline layoutInflater: (parent: ViewGroup, layoutRes: Int) -> View = { parent, layoutRes ->
        LayoutInflater.from(parent.context).inflate(
            layoutRes,
            parent,
            false
        )
    },
    noinline block: AdapterDelegateLayoutContainerViewHolder<I>.() -> Unit
): AdapterDelegate<AdapterItemProvider<T>> {

    return DslLayoutContainerAdapterDelegate(
        layout = layout,
        on = on,
        initializerBlock = block,
        layoutInflater = layoutInflater
    )
}

@PublishedApi
internal class DslLayoutContainerAdapterDelegate<I : T, T>(
    @LayoutRes private val layout: Int,
    private val on: (item: T, items: AdapterItemProvider<T>, position: Int) -> Boolean,
    private val initializerBlock: AdapterDelegateLayoutContainerViewHolder<I>.() -> Unit,
    private val layoutInflater: (parent: ViewGroup, layoutRes: Int) -> View
) : AbsItemAdapterDelegate<I, T, AdapterDelegateLayoutContainerViewHolder<I>>() {

    override fun isForViewType(item: T, items: AdapterItemProvider<T>, position: Int): Boolean = on(
        item, items, position
    )

    override fun onCreateViewHolder(parent: ViewGroup): AdapterDelegateLayoutContainerViewHolder<I> =
        AdapterDelegateLayoutContainerViewHolder<I>(
            layoutInflater(parent, layout)
        ).also {
            initializerBlock(it)
        }

    override fun onBindViewHolder(
        item: I,
        holder: AdapterDelegateLayoutContainerViewHolder<I>,
        payloads: List<Any>
    ) {
        holder._item = item as Any
        holder._bind?.invoke(payloads) // It's ok to have an AdapterDelegate without binding block (i.e. static content)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        @Suppress("UNCHECKED_CAST")
        val vh = (holder as AdapterDelegateLayoutContainerViewHolder<I>)

        vh._onViewRecycled?.invoke()
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        @Suppress("UNCHECKED_CAST")
        val vh = (holder as AdapterDelegateLayoutContainerViewHolder<I>)
        val block = vh._onFailedToRecycleView
        return if (block == null) {
            super.onFailedToRecycleView(holder)
        } else {
            block()
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        @Suppress("UNCHECKED_CAST")
        val vh = (holder as AdapterDelegateLayoutContainerViewHolder<I>)
        vh._onViewAttachedToWindow?.invoke()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        @Suppress("UNCHECKED_CAST")
        val vh = (holder as AdapterDelegateLayoutContainerViewHolder<I>)
        vh._onViewDetachedFromWindow?.invoke()
    }
}
