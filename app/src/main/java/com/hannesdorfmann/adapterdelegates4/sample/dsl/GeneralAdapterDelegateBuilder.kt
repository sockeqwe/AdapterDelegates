package com.hannesdorfmann.adapterdelegates4.sample.dsl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import java.lang.IllegalArgumentException

/**
 * Builder for general [AdapterDelegate] (that are not necessarily backed by a List of items)
 */
fun <T> generalAdapterDelegate(block: GeneralAdapterDelegateBuilder<T>.() -> Unit): AdapterDelegate<T> {

    val builder = GeneralAdapterDelegateBuilder<T>()
    block(builder)

    // TODO better checks
    val on = builder.on ?: throw IllegalArgumentException("on condition must be set")

    val layout =
        builder.layout
            ?: throw IllegalArgumentException("Layout resource is not set in generalAdapterDelegate builder block")

    val bindingBlock =
        builder.bindingBlock ?: throw IllegalArgumentException("binding block not set in generalAdapterDelegate block")

    return object : AdapterDelegate<T>() {

        override fun isForViewType(items: T, position: Int): Boolean = on(items, position)

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            GeneralAdapterDelegateBuilder.AdapterDelegateBuilderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    layout,
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(
            items: T,
            position: Int,
            holder: RecyclerView.ViewHolder,
            payloads: MutableList<Any>
        ) {
            bindingBlock(
                holder as GeneralAdapterDelegateBuilder.AdapterDelegateBuilderViewHolder,
                items,
                position,
                payloads
            )
        }
    }
}

/**
 * Used with [generalAdapterDelegate] to build an AdapterDelegate.
 * Don't instantiate this directly, use [generalAdapterDelegate] function instead.
 */
class GeneralAdapterDelegateBuilder<T> {

    var on: ((items: T, position: Int) -> Boolean)? = null

    @LayoutRes
    var layout: Int? = null

    internal var bindingBlock: (
    AdapterDelegateBuilderViewHolder.(
        item: T,
        position: Int,
        payloads: MutableList<Any>
    ) -> Unit
    )? = null

    fun bind(
        bindingBlock: AdapterDelegateBuilderViewHolder.(
            item: T,
            position: Int,
            payloads: MutableList<Any>
        ) -> Unit
    ) {
        this.bindingBlock = bindingBlock
    }

    class AdapterDelegateBuilderViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer{
        val context = containerView.context
    }
}
