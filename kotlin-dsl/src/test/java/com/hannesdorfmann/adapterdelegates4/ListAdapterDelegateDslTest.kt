package com.hannesdorfmann.adapterdelegates4

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateViewHolder
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import java.lang.IllegalStateException

class ListAdapterDelegateDslTest {

    @Test
    fun `init block is called once - bind multiple times`() {
        val viewGroup = Mockito.mock(ViewGroup::class.java)
        val view = Mockito.mock(View::class.java)
        val layoutToInflate = 999
        val items = listOf(Any(), Any())
        var payload = emptyList<Any>()

        var viewHolder: AdapterDelegateViewHolder<Any>? = null

        var initCalled = 0
        var bindCalled = 0
        var bindPayload = emptyList<Any>()
        var boundItemInBindBlock: Any? = null

        val delegate = adapterDelegate<Any, Any>(layoutToInflate,
            layoutInflater = { parent, layoutRes ->
                Assert.assertSame(viewGroup, parent)
                Assert.assertEquals(layoutToInflate, layoutRes)
                view
            }) {
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

        val delegate = adapterDelegate<Item, Any>(0) {
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

        val delegate = adapterDelegate<Item, Any>(
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
        try {
            adapterDelegate<Any, Any>(0) {
                bind { }

                bind { }
            }
        } catch (e: IllegalStateException) {
            val expectedMsg = "bind { ... } is already defined. Only one bind block is allowed."
            Assert.assertEquals(expectedMsg, e.message)
        }
    }

    data class Item(val name: String)
}