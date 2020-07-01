package com.hannesdorfmann.adapterdelegates4.dsl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.hannesdorfmann.adapterdelegates4.AbsItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterItemProvider

inline fun <reified  I : T, T, V : ViewBinding> adapterViewBinding(
    noinline viewBinding: (layoutInflater: LayoutInflater, parent: ViewGroup) -> V,
    noinline on: (item: T, items: AdapterItemProvider<T>, position: Int) -> Boolean = { item, _, _ -> item is I},
    noinline layoutInflater: (parent: ViewGroup) -> LayoutInflater = { parent -> LayoutInflater.from(parent.context)},
    noinline block: AdapterDelegateViewBindingViewHolder<I, V>.() -> Unit
): AdapterDelegate<AdapterItemProvider<T>> {
    return DslViewBindingAdapterDelegate(
        binding = viewBinding,
        on = on,
        initializerBlock = block,
        layoutInflater = layoutInflater
    )
}

@PublishedApi
internal class DslViewBindingAdapterDelegate<I : T, T, V : ViewBinding>(
    private val binding: (layoutInflater: LayoutInflater, parent: ViewGroup) -> V,
    private val on: (item: T, items: AdapterItemProvider<T>, position: Int) -> Boolean,
    private val initializerBlock: (AdapterDelegateViewBindingViewHolder<I, V>) -> Unit,
    private val layoutInflater: (parent: ViewGroup) -> LayoutInflater
) : AbsItemAdapterDelegate<I, T, AdapterDelegateViewBindingViewHolder<I, V>>() {

    override fun isForViewType(item: T, items: AdapterItemProvider<T>, position: Int): Boolean = on(
        item, items, position
    )

    override fun onCreateViewHolder(parent: ViewGroup): AdapterDelegateViewBindingViewHolder<I, V> {
        val binding = binding(layoutInflater(parent), parent)
        return AdapterDelegateViewBindingViewHolder<I, V>(
            binding
        ).also {
            initializerBlock(it)
        }
    }

    override fun onBindViewHolder(item: I, holder: AdapterDelegateViewBindingViewHolder<I, V>, payloads: List<Any>) {
        holder._item = item as Any
        holder._bind?.invoke(payloads) // It's ok to have an AdapterDelegate without binding block (i.e. static content)
    }
}
