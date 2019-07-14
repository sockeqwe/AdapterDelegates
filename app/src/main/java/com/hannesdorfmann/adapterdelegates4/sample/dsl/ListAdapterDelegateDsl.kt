package com.hannesdorfmann.adapterdelegates4.sample.dsl

import android.system.Os.bind
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.sample.R
import com.hannesdorfmann.adapterdelegates4.sample.model.Cat
import com.hannesdorfmann.adapterdelegates4.sample.model.DisplayableItem
import kotlinx.android.synthetic.main.item_cat.*
import kotlin.IllegalArgumentException

/**
 * Simple DSL builder to create an [AdapterDelegate] that is backed by a [List] as dataset.
 */
inline fun <reified I : T, T> delegate(
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
        holder._bindingBlock!!(payloads)
    }
}

class AdapterDelegateViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {

    internal object Uninitialized

    /**
     * Used only internally to set the item.
     * The outside consumer should always access [item].
     * We do that to trick the user for his own convenience since the Item is only available later and is actually var
     * (not val) but we rely on mechanisms from RecyclerView and assume that only the main thread can access and set
     * this field (as the user scrolls) so that we make [item] look like a val.
     */
    internal var _item: Any = Uninitialized

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
     * This should never be called.
     * Use [bind] instead which internally sets this field.
     */
    internal var _bindingBlock: ((payloads: List<Any>) -> Unit)? = null

    fun bind(bindingBlock: (payloads: List<Any>) -> Unit) {
        this._bindingBlock = bindingBlock
    }

    fun <V : View> findViewById(@IdRes id: Int): V =
        itemView.findViewById(id) as V
}

// Example
val fcatDelegate = delegate<Cat, DisplayableItem>(R.layout.item_cat) {

    val name = findViewById<TextView>(R.id.name)
    name.setOnClickListener {
        Log.d("Click", "Click on $item")
    }

    bind {
        name.text = item.name
    }
}
