package com.hannesdorfmann.adapterdelegates4.sample.dsl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import java.lang.IllegalStateException
import kotlin.IllegalArgumentException

/**
 * Simple DSL builder to create an [AdapterDelegate] that is backed by a [List] as dataset.
 *
 * @param layout The android xml layout resource that contains the layout for this adapter delegate.
 * @param on The check that should be run if the AdapterDelegate is for the corresponding Item in the datasource.
 * In other words its the implementation of [AdapterDelegate.isForViewType].
 * @param block The DSL block. Specify here what to do when the ViewHolder gets created. Think of it as some kind of
 * initializer block. For example, you would setup a click listener on a Ui widget in that block followed by specifying
 * what to do once the ViewHolder binds to the data by specifying a bind block for
 * @since 4.1.0
 */
inline fun <reified I : T, T> adapterDelegate(
    @LayoutRes layout: Int,
    noinline on: (item: T, items: List<T>, position: Int) -> Boolean = { item, _, _ -> item is I },
    noinline block: AdapterDelegateViewHolder<I>.() -> Unit
): AdapterDelegate<List<T>> {

    return DslListAdapterDelegate(
        layout = layout,
        on = on,
        intializerBlock = block
    )
}

class DslListAdapterDelegate<I : T, T>(
    @LayoutRes private val layout: Int,
    private val on: (item: T, items: List<T>, position: Int) -> Boolean,
    private val intializerBlock: AdapterDelegateViewHolder<I>.() -> Unit
) : AbsListItemAdapterDelegate<I, T, AdapterDelegateViewHolder<I>>() {

    override fun isForViewType(item: T, items: MutableList<T>, position: Int): Boolean = on(
        item, items, position
    )

    override fun onCreateViewHolder(parent: ViewGroup): AdapterDelegateViewHolder<I> =
        AdapterDelegateViewHolder<I>(
            LayoutInflater.from(parent.context).inflate(
                layout,
                parent,
                false
            )
        ).also {
            intializerBlock(it)
        }

    override fun onBindViewHolder(
        item: I,
        holder: AdapterDelegateViewHolder<I>,
        payloads: MutableList<Any>
    ) {
        holder._item = item as Any
        holder._bindingBlock?.invoke(payloads) // It's ok to have an AdapterDelegate without binding block (i.e. static content)
    }
}

/**
 * ViewHolder that is used internally if you use [adapterDelegate] DSL to create your Adapter
 */
open class AdapterDelegateViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {

    // TODO private?
    internal object Uninitialized

    /**
     * Used only internally to set the item.
     * The outside consumer should always access [item].
     * We do that to trick the user for his own convenience since the Item is only available later and is actually var
     * (not val) but we rely on mechanisms from RecyclerView and assume that only the main thread can access and set
     * this field (as the user scrolls) so that we make [item] look like a val.
     */
    internal var _item: Any = Uninitialized

    /**
     * Get the current bound item.
     */
    val item: T
        get() = if (_item === Uninitialized) {
            throw IllegalArgumentException(
                "Item has not been set yet. That is an internal issue. " +
                    "Please report at https://github.com/sockeqwe/AdapterDelegates"
            )
        } else {
            @Suppress("UNCHECKED_CAST")
            _item as T
        }

    /**
     * This should never be called directly.
     * Use [bind] instead which internally sets this field.
     */
    internal var _bindingBlock: ((payloads: List<Any>) -> Unit)? = null

    /**
     * Define here the block that should be run whenever the viewholder get binded.
     * You can access the current bound item with [item]. In case you need the position of the bound item inside the
     * adapters dataset use [getAdapterPosition].
     */
    fun bind(bindingBlock: (payloads: List<Any>) -> Unit) {
        if (_bindingBlock != null) {
            throw IllegalStateException("bind { ... } is already defined. Only one bind block is allowed.")
        }
        this._bindingBlock = bindingBlock
    }

    /**
     * Convenience method find a given view with the given id inside the layout
     */
    fun <V : View> findViewById(@IdRes id: Int): V = itemView.findViewById(id) as V
}
