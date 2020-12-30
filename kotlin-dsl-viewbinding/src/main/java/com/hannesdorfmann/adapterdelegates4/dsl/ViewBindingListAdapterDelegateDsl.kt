package com.hannesdorfmann.adapterdelegates4.dsl

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

/**
 * Simple DSL builder to create an [AdapterDelegate] that is backed by a [List] as dataset.
 * This DSL builds on top of [ViewBinding] so that no findViewById is needed anymore.
 *
 * @param viewBinding return a [ViewBinding] for this adapter delegate. Typically a method reference. For
 * example MyBinding::inflate
 * @param on The check that should be run if the AdapterDelegate is for the corresponding Item in the datasource.
 * In other words its the implementation of [AdapterDelegate.isForViewType].
 * @param block The DSL block. Specify here what to do when the ViewHolder gets created. Think of it as some kind of
 * initializer block. For example, you would setup a click listener on a Ui widget in that block followed by specifying
 * what to do once the ViewHolder binds to the data by specifying a bind block for
 * @since 4.3.0
 */
inline fun <reified I : T, T, V : ViewBinding> adapterDelegateViewBinding(
    noinline viewBinding: (layoutInflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) -> V,
    noinline on: (item: T, items: List<T>, position: Int) -> Boolean = { item, _, _ -> item is I },
    noinline layoutInflater: (parent: ViewGroup) -> LayoutInflater = { parent -> LayoutInflater.from(parent.context) },
    noinline block: AdapterDelegateViewBindingViewHolder<I, V>.() -> Unit
): AdapterDelegate<List<T>> {

    return DslViewBindingListAdapterDelegate(
        binding = viewBinding,
        on = on,
        initializerBlock = block,
        layoutInflater = layoutInflater)
}


@PublishedApi
internal class DslViewBindingListAdapterDelegate<I : T, T, V : ViewBinding>(
    private val binding: (layoutInflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) -> V,
    private val on: (item: T, items: List<T>, position: Int) -> Boolean,
    private val initializerBlock: AdapterDelegateViewBindingViewHolder<I, V>.() -> Unit,
    private val layoutInflater: (parent: ViewGroup) -> LayoutInflater
) : AbsListItemAdapterDelegate<I, T, AdapterDelegateViewBindingViewHolder<I, V>>() {

    override fun isForViewType(item: T, items: MutableList<T>, position: Int): Boolean = on(
        item, items, position
    )

    override fun onCreateViewHolder(parent: ViewGroup): AdapterDelegateViewBindingViewHolder<I, V> {
        val binding = binding(layoutInflater(parent), parent, false)
        return AdapterDelegateViewBindingViewHolder<I, V>(
            binding
        ).also {
            initializerBlock(it)
        }
    }

    override fun onBindViewHolder(
        item: I,
        holder: AdapterDelegateViewBindingViewHolder<I, V>,
        payloads: MutableList<Any>
    ) {
        holder._item = item as Any
        holder._bind?.invoke(payloads) // It's ok to have an AdapterDelegate without binding block (i.e. static content)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        @Suppress("UNCHECKED_CAST")
        val vh = (holder as AdapterDelegateViewBindingViewHolder<I, V>)

        vh._onViewRecycled?.invoke()
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        @Suppress("UNCHECKED_CAST")
        val vh = (holder as AdapterDelegateViewBindingViewHolder<I, V>)
        val block = vh._onFailedToRecycleView
        return if (block == null) {
            super.onFailedToRecycleView(holder)
        } else {
            block()
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        @Suppress("UNCHECKED_CAST")
        val vh = (holder as AdapterDelegateViewBindingViewHolder<I, V>)
        vh._onViewAttachedToWindow?.invoke()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        @Suppress("UNCHECKED_CAST")
        val vh = (holder as AdapterDelegateViewBindingViewHolder<I, V>)
        vh._onViewDetachedFromWindow?.invoke()
    }
}

/**
 * ViewHolder that is used internally if you use [adapterDelegateViewBinding] DSL to create your AdapterDelegate
 *
 * @since 4.3.0
 */
class AdapterDelegateViewBindingViewHolder<T, V : ViewBinding>(
    val binding: V, view: View = binding.root
) : RecyclerView.ViewHolder(view) {

    private object Uninitialized

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
     * Get the context.
     *
     * @since 4.3.0
     */
    val context: Context = view.context

    /**
     * Returns a localized string from the application's package's
     * default string table.
     *
     * @param resId Resource id for the string
     * @return The string data associated with the resource, stripped of styled
     * text information.
     *
     * @since 4.3.0
     */
    fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    /**
     * Returns a localized formatted string from the application's package's
     * default string table, substituting the format arguments as defined in
     * [java.util.Formatter] and [java.lang.String.format].
     *
     * @param resId Resource id for the format string
     * @param formatArgs The format arguments that will be used for
     * substitution.
     * @return The string data associated with the resource, formatted and
     * stripped of styled text information.
     *
     * @since 4.3.0
     */
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }

    /**
     * Returns a color associated with a particular resource ID and styled for
     * the current theme.
     *
     * @param id The desired resource identifier, as generated by the aapt
     * tool. This integer encodes the package, type, and resource
     * entry. The value 0 is an invalid identifier.
     * @return A single color value in the form 0xAARRGGBB.
     * @throws android.content.res.Resources.NotFoundException if the given ID
     * does not exist.
     *
     * @since 4.3.0
     */
    @ColorInt
    fun getColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(context, id)
    }

    /**
     * Returns a drawable object associated with a particular resource ID and
     * styled for the current theme.
     *
     * @param id The desired resource identifier, as generated by the aapt
     * tool. This integer encodes the package, type, and resource
     * entry. The value 0 is an invalid identifier.
     * @return An object that can be used to draw this resource.
     * @throws android.content.res.Resources.NotFoundException if the given ID
     * does not exist.
     *
     * @since 4.3.0
     */
    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(context, id)
    }

    /**
     * Returns a color state list associated with a particular resource ID and
     * styled for the current theme.
     *
     * @param id The desired resource identifier, as generated by the aapt
     * tool. This integer encodes the package, type, and resource
     * entry. The value 0 is an invalid identifier.
     * @return A color state list.
     * @throws android.content.res.Resources.NotFoundException if the given ID
     * does not exist.
     */
    fun getColorStateList(@ColorRes id: Int): ColorStateList? {
        return ContextCompat.getColorStateList(context, id)
    }

    /**
     * This should never be called directly.
     * Use [bind] instead which internally sets this field.
     */
    internal var _bind: ((payloads: List<Any>) -> Unit)? = null
        private set

    /**
     * This should never be called directly (only called internally)
     * Use [onViewRecycled] instead
     */
    internal var _onViewRecycled: (() -> Unit)? = null
        private set

    /**
     * This should never be called directly (only called internally)
     * Use [onFailedToRecycleView] instead.
     */
    internal var _onFailedToRecycleView: (() -> Boolean)? = null
        private set

    /**
     * This should never be called directly (only called internally)
     * Use [onViewAttachedToWindow] instead.
     */
    internal var _onViewAttachedToWindow: (() -> Unit)? = null
        private set

    /**
     * This should never be called directly (only called internally)
     * Use [onViewDetachedFromWindow] instead.
     */
    internal var _onViewDetachedFromWindow: (() -> Unit)? = null
        private set

    /**
     * Define here the block that should be run whenever the viewholder get binded.
     * You can access the current bound item with [item]. In case you need the position of the bound item inside the
     * adapters dataset use [getAdapterPosition].
     */
    fun bind(bindingBlock: (payloads: List<Any>) -> Unit) {
        if (_bind != null) {
            throw IllegalStateException("bind { ... } is already defined. Only one bind { ... } is allowed.")
        }
        _bind = bindingBlock
    }

    /**
     * @see AdapterDelegate.onViewRecycled
     */
    fun onViewRecycled(block: () -> Unit) {
        if (_onViewRecycled != null) {
            throw IllegalStateException(
                "onViewRecycled { ... } is already defined. " +
                        "Only one onViewRecycled { ... } is allowed."
            )
        }
        _onViewRecycled = block
    }

    /**
     * @see AdapterDelegate.onFailedToRecycleView
     */
    fun onFailedToRecycleView(block: () -> Boolean) {
        if (_onFailedToRecycleView != null) {
            throw IllegalStateException(
                "onFailedToRecycleView { ... } is already defined. " +
                        "Only one onFailedToRecycleView { ... } is allowed."
            )
        }
        _onFailedToRecycleView = block
    }

    /**
     * @see AdapterDelegate.onViewAttachedToWindow
     */
    fun onViewAttachedToWindow(block: () -> Unit) {
        if (_onViewAttachedToWindow != null) {
            throw IllegalStateException(
                "onViewAttachedToWindow { ... } is already defined. " +
                        "Only one onViewAttachedToWindow { ... } is allowed."
            )
        }
        _onViewAttachedToWindow = block
    }

    /**
     * @see AdapterDelegate.onViewDetachedFromWindow
     */
    fun onViewDetachedFromWindow(block: () -> Unit) {
        if (_onViewDetachedFromWindow != null) {
            throw IllegalStateException(
                "onViewDetachedFromWindow { ... } is already defined. " +
                        "Only one onViewDetachedFromWindow { ... } is allowed."
            )
        }
        _onViewDetachedFromWindow = block
    }
}
