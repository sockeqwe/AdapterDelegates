package com.hannesdorfmann.adapterdelegates4

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateLayoutContainerViewHolder
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when` as whenever

class LayoutContainerListAdapterDelegateDslTest {

    data class Item(val name: String)

    private fun fakeLayoutInflater(layoutToInflate: Int): Pair<(ViewGroup, Int) -> View, ViewGroup> {
        val viewGroup = Mockito.mock(ViewGroup::class.java)
        val view = Mockito.mock(View::class.java)
        val context = Mockito.mock(Context::class.java)
        whenever(view.context).thenReturn(context)

        val inflater = { parent: ViewGroup, layoutRes: Int ->
            Assert.assertSame(viewGroup, parent)
            Assert.assertEquals(layoutToInflate, layoutRes)
            view
        }

        return Pair(inflater, viewGroup)
    }

    @Test
    fun `init block is called once - bind multiple times`() {

        val layoutToInflate = 999
        val (inflater, viewGroup) = fakeLayoutInflater(layoutToInflate)
        val items = listOf(Any(), Any())
        var payload = emptyList<Any>()

        var viewHolder: AdapterDelegateLayoutContainerViewHolder<Any>? = null

        var initCalled = 0
        var bindCalled = 0
        var bindPayload = emptyList<Any>()
        var boundItemInBindBlock: Any? = null

        val delegate = adapterDelegateLayoutContainer<Any, Any>(
            layoutToInflate,
            layoutInflater = inflater
        ) {
            initCalled++
            viewHolder = this
            bind {
                bindCalled++
                bindPayload = it
                boundItemInBindBlock = item
            }
        }

        // Assert init block is called
        delegate.onCreateViewHolder(viewGroup)
        Assert.assertEquals(1, initCalled)
        Assert.assertNotNull(viewHolder)

        // Assert binding at position 0
        delegate.onBindViewHolder(items, 0, viewHolder!!, payload)
        Assert.assertEquals(1, bindCalled)
        Assert.assertSame(payload, bindPayload)
        Assert.assertSame(items[0], viewHolder!!.item)
        Assert.assertSame(items[0], boundItemInBindBlock)

        // Assert binding at position 1
        payload = listOf(Any())
        delegate.onBindViewHolder(items, 1, viewHolder!!, payload)
        Assert.assertEquals(2, bindCalled)
        Assert.assertSame(payload, bindPayload)
        Assert.assertSame(items[1], viewHolder!!.item)
        Assert.assertSame(items[1], boundItemInBindBlock)
    }

    @Test
    fun `isForViewType is determined from generics correctly`() {

        val delegate = adapterDelegateLayoutContainer<Item, Any>(0) {
        }

        val item = Item("foo")
        val items = listOf<Any>(item, Any())

        Assert.assertTrue(delegate.isForViewType(items, 0))
        Assert.assertFalse(delegate.isForViewType(items, 1))
    }

    @Test
    fun `custom on block is used for isForViewType`() {

        var onBlockCalled = 0
        var onBlockItem: Any? = null
        var onBlockList: List<Any>? = null
        var onBlockPosition = -1

        val delegate = adapterDelegateLayoutContainer<Item, Any>(
            layout = 0,
            on = { item, list, position ->
                onBlockItem = item
                onBlockList = list
                onBlockPosition = position
                onBlockCalled++
                false
            }) {

        }

        val item = Item("foo")
        val items = listOf<Any>(item, Any())

        Assert.assertFalse(delegate.isForViewType(items, 0))
        Assert.assertEquals(1, onBlockCalled)
        Assert.assertSame(item, onBlockItem)
        Assert.assertSame(items, onBlockList)
        Assert.assertEquals(0, onBlockPosition)

        Assert.assertFalse(delegate.isForViewType(items, 1))
        Assert.assertEquals(2, onBlockCalled)
        Assert.assertSame(items[1], onBlockItem)
        Assert.assertSame(items, onBlockList)
        Assert.assertEquals(1, onBlockPosition)
    }

    @Test
    fun `multiple binds throws exception`() {
        val layoutToInflate = 0
        val (inflater, viewGroup) = fakeLayoutInflater(layoutToInflate)

        try {
            val delegate = adapterDelegateLayoutContainer<Any, Any>(
                layout = layoutToInflate,
                layoutInflater = inflater
            ) {
                bind { }

                bind { }
            }
            delegate.onCreateViewHolder(viewGroup)
            Assert.fail("Exception expected")
        } catch (e: IllegalStateException) {
            val expectedMsg = "bind { ... } is already defined. Only one bind { ... } is allowed."
            Assert.assertEquals(expectedMsg, e.message)
        }
    }

    @Test
    fun `onViewRecycled called`() {
        val (inflater, viewGroup) = fakeLayoutInflater(0)
        var called = 0
        var viewHolder: AdapterDelegateLayoutContainerViewHolder<Any>? = null
        val delegate = adapterDelegateLayoutContainer<Any, Any>(
            layout = 0,
            layoutInflater = inflater
        ) {
            viewHolder = this
            onViewRecycled {
                called++
            }
        }

        delegate.onCreateViewHolder(viewGroup)
        Assert.assertNotNull(viewHolder)
        delegate.onViewRecycled(viewHolder!!)
        Assert.assertEquals(1, called)
    }

    @Test
    fun `multiple onViewRecycled throws exception`() {
        val layoutToInflate = 0
        val (inflater, viewGroup) = fakeLayoutInflater(layoutToInflate)

        try {
            val delegate = adapterDelegateLayoutContainer<Any, Any>(
                layout = layoutToInflate,
                layoutInflater = inflater
            ) {
                onViewRecycled { }

                onViewRecycled { }
            }
            delegate.onCreateViewHolder(viewGroup)
            Assert.fail("Exception expected")
        } catch (e: IllegalStateException) {
            val expectedMsg =
                "onViewRecycled { ... } is already defined. Only one onViewRecycled { ... } is allowed."
            Assert.assertEquals(expectedMsg, e.message)
        }
    }

    @Test
    fun `onFailedToRecycleView called`() {
        val (inflater, viewGroup) = fakeLayoutInflater(0)
        var called = 0
        var viewHolder: AdapterDelegateLayoutContainerViewHolder<Any>? = null
        val delegate = adapterDelegateLayoutContainer<Any, Any>(
            layout = 0,
            layoutInflater = inflater
        ) {
            viewHolder = this
            onFailedToRecycleView {
                called++
                true
            }
        }

        delegate.onCreateViewHolder(viewGroup)
        Assert.assertNotNull(viewHolder)
        val ret = delegate.onFailedToRecycleView(viewHolder!!)
        Assert.assertEquals(1, called)
        Assert.assertTrue(ret)
    }

    @Test
    fun `multiple onFailedToRecycleView throws exception`() {
        val layoutToInflate = 0
        val (inflater, viewGroup) = fakeLayoutInflater(layoutToInflate)

        try {
            val delegate = adapterDelegateLayoutContainer<Any, Any>(
                layout = layoutToInflate,
                layoutInflater = inflater
            ) {
                onFailedToRecycleView { false }

                onFailedToRecycleView { false }
            }
            delegate.onCreateViewHolder(viewGroup)
            Assert.fail("Exception expected")
        } catch (e: IllegalStateException) {
            val expectedMsg =
                "onFailedToRecycleView { ... } is already defined. Only one onFailedToRecycleView { ... } is allowed."
            Assert.assertEquals(expectedMsg, e.message)
        }
    }

    @Test
    fun `onViewAttachedToWindow called`() {
        val (inflater, viewGroup) = fakeLayoutInflater(0)
        var called = 0
        var viewHolder: AdapterDelegateLayoutContainerViewHolder<Any>? = null
        val delegate = adapterDelegateLayoutContainer<Any, Any>(
            layout = 0,
            layoutInflater = inflater
        ) {
            viewHolder = this
            onViewAttachedToWindow {
                called++
            }
        }

        delegate.onCreateViewHolder(viewGroup)
        Assert.assertNotNull(viewHolder)
        delegate.onViewAttachedToWindow(viewHolder!!)
        Assert.assertEquals(1, called)
    }

    @Test
    fun `multiple onViewAttachedToWindow throws exception`() {
        val layoutToInflate = 0
        val (inflater, viewGroup) = fakeLayoutInflater(layoutToInflate)

        try {
            val delegate = adapterDelegateLayoutContainer<Any, Any>(
                layout = layoutToInflate,
                layoutInflater = inflater
            ) {
                onViewAttachedToWindow { }

                onViewAttachedToWindow { }
            }
            delegate.onCreateViewHolder(viewGroup)
            Assert.fail("Exception expected")
        } catch (e: IllegalStateException) {
            val expectedMsg =
                "onViewAttachedToWindow { ... } is already defined. Only one onViewAttachedToWindow { ... } is allowed."
            Assert.assertEquals(expectedMsg, e.message)
        }
    }


    @Test
    fun `onViewDetachedFromWindow called`() {
        val (inflater, viewGroup) = fakeLayoutInflater(0)
        var called = 0
        var viewHolder: AdapterDelegateLayoutContainerViewHolder<Any>? = null
        val delegate = adapterDelegateLayoutContainer<Any, Any>(
            layout = 0,
            layoutInflater = inflater
        ) {
            viewHolder = this
            onViewDetachedFromWindow {
                called++
            }
        }

        delegate.onCreateViewHolder(viewGroup)
        Assert.assertNotNull(viewHolder)
        delegate.onViewDetachedFromWindow(viewHolder!!)
        Assert.assertEquals(1, called)
    }

    @Test
    fun `multiple onViewDetachedFromWindow throws exception`() {
        val layoutToInflate = 0
        val (inflater, viewGroup) = fakeLayoutInflater(layoutToInflate)

        try {
            val delegate = adapterDelegateLayoutContainer<Any, Any>(
                layout = layoutToInflate,
                layoutInflater = inflater
            ) {
                onViewDetachedFromWindow { }

                onViewDetachedFromWindow { }
            }
            delegate.onCreateViewHolder(viewGroup)
            Assert.fail("Exception expected")
        } catch (e: IllegalStateException) {
            val expectedMsg =
                "onViewDetachedFromWindow { ... } is already defined. Only one onViewDetachedFromWindow { ... } is allowed."
            Assert.assertEquals(expectedMsg, e.message)
        }
    }
}