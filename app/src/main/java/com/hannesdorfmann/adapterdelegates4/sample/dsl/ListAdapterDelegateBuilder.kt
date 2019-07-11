package com.hannesdorfmann.adapterdelegates4.sample.dsl

import android.system.Os.bind
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.sample.R
import com.hannesdorfmann.adapterdelegates4.sample.model.Cat
import com.hannesdorfmann.adapterdelegates4.sample.model.DisplayableItem
import kotlinx.android.synthetic.main.item_cat.*
import java.lang.IllegalArgumentException
import kotlin.reflect.typeOf

/**
 * Simple DSL builder to create an [AdapterDelegate] that is backed by a [List] as dataset.
 */
inline fun <reified I : T, T> adapterDelegate(block: ListAdapterDelegateBuilder<I, T>.() -> Unit): AdapterDelegate<List<T>> {

    val builder = ListAdapterDelegateBuilder<I, T>(
        on = { item, _, _ -> item is I }
    )
    block(builder)

    // TODO better exceptions
    val on = builder.on
    val layout =
        builder.layout
            ?: throw IllegalArgumentException("Layout resource is not set in generalAdapterDelegate builder block")

    val bindingBlock =
        builder._bindingBlock ?: throw IllegalArgumentException("binding block not set in generalAdapterDelegate block")

    return object : AbsListItemAdapterDelegate<I, T, GeneralAdapterDelegateBuilder.AdapterDelegateBuilderViewHolder>() {
        override fun isForViewType(item: T, items: MutableList<T>, position: Int): Boolean = on(
            item, items, position
        )

        override fun onCreateViewHolder(parent: ViewGroup): GeneralAdapterDelegateBuilder.AdapterDelegateBuilderViewHolder =
            GeneralAdapterDelegateBuilder.AdapterDelegateBuilderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    layout,
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(
            item: I,
            holder: GeneralAdapterDelegateBuilder.AdapterDelegateBuilderViewHolder,
            payloads: MutableList<Any>
        ) {
            bindingBlock(
                holder,
                item,
                payloads
            )
        }
    }
}

/**
 * Internally used for creating a DSL. You should not use this class directly nor instantiate it directly.
 * Use [adapterDelegate] function instead.
 */
class ListAdapterDelegateBuilder<I : T, T>(
    var on: ((item: T, items: List<T>, position: Int) -> Boolean)
) {

    @LayoutRes
    var layout: Int? = null

    /**
     * This should never be called.
     * Use [bind] instead which internally sets this field.
     */
    var _bindingBlock: (
    GeneralAdapterDelegateBuilder.AdapterDelegateBuilderViewHolder.(
        item: I,
        payloads: MutableList<Any>
    ) -> Unit
    )? = null
        private set

    fun bind(
        bindingBlock: GeneralAdapterDelegateBuilder.AdapterDelegateBuilderViewHolder.(
            item: I,
            payloads: MutableList<Any>
        ) -> Unit
    ) {
        this._bindingBlock = bindingBlock
    }
}

// Example
val catDelegate = adapterDelegate<Cat, DisplayableItem> {

    layout = R.layout.item_cat

    bind { item, _ ->
        name.text = item.name
    }
}
