package com.hannesdorfmann.adapterdelegates4

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateViewBindingViewHolder
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when` as whenever

class ViewBindingListAdapterDelegateDslTest {

    data class Item(val name: String)

    private fun fakeLayoutInflater(): LayoutInflater {
        return Mockito.mock(LayoutInflater::class.java)
    }

    private fun fakeView(): View {
        val view = Mockito.mock(View::class.java)
        val context = Mockito.mock(Context::class.java)
        whenever(view.context).thenReturn(context)
        return view
    }

    private fun fakeViewGroup(): ViewGroup {
        return Mockito.mock(ViewGroup::class.java)
    }

    @Test
    fun `init block is called once - bind multiple times`() {

        val items = listOf(Any(), Any())
        var payload = emptyList<Any>()

        var viewHolder: AdapterDelegateViewBindingViewHolder<Any, ViewBinding>? = null

        var initCalled = 0
        var bindCalled = 0
        var bindPayload = emptyList<Any>()
        var boundItemInBindBlock: Any? = null
        val view = fakeView()
        val viewGroup = fakeViewGroup()
        val layoutInflater = fakeLayoutInflater()

        val binding = ViewBinding {
            view
        }
        val delegate =
            adapterDelegateViewBinding<Any, Any, ViewBinding>(
                viewBinding = { _, _ -> binding },
                layoutInflater = { layoutInflater }
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
        val view = fakeView()

        val binding = ViewBinding {
            view
        }
        val delegate =
            adapterDelegateViewBinding<Item, Any, ViewBinding>(
                viewBinding = { _, _ -> binding }) {
            }

        val item = Item("foo")
        val items = listOf(item, Any())

        Assert.assertTrue(delegate.isForViewType(items, 0))
        Assert.assertFalse(delegate.isForViewType(items, 1))
    }

    @Test
    fun `custom on block is used for isForViewType`() {
        val view = fakeView()

        var onBlockCalled = 0
        var onBlockItem: Any? = null
        var onBlockList: List<Any>? = null
        var onBlockPosition = -1

        val binding = ViewBinding {
            view
        }
        val delegate =
            adapterDelegateViewBinding<Item, Any, ViewBinding>(
                viewBinding = { _, _ -> binding },
                on = { item, list, position ->
                    onBlockItem = item
                    onBlockList = list
                    onBlockPosition = position
                    onBlockCalled++
                    false
                }) {

            }

        val item = Item("foo")
        val items = listOf(item, Any())

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
        val view = fakeView()
        val viewGroup = fakeViewGroup()
        val layoutInflater = fakeLayoutInflater()

        val binding = ViewBinding {
            view
        }
        try {
            val delegate =
                adapterDelegateViewBinding<Any, Any, ViewBinding>(
                    viewBinding = { _, _ -> binding },
                    layoutInflater = { layoutInflater }
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
        val view = fakeView()
        val viewGroup = fakeViewGroup()
        val layoutInflater = fakeLayoutInflater()
        var called = 0
        var viewHolder: AdapterDelegateViewBindingViewHolder<Any, ViewBinding>? = null
        val binding = ViewBinding {
            view
        }
        val delegate =
            adapterDelegateViewBinding<Any, Any, ViewBinding>(
                viewBinding = { _, _ -> binding },
                layoutInflater = { layoutInflater }
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
        val view = fakeView()
        val viewGroup = fakeViewGroup()
        val layoutInflater = fakeLayoutInflater()

        val binding = ViewBinding {
            view
        }
        try {
            val delegate =
                adapterDelegateViewBinding<Any, Any, ViewBinding>(
                    viewBinding = { _, _ -> binding },
                    layoutInflater = { layoutInflater }
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
        val view = fakeView()
        val viewGroup = fakeViewGroup()
        val layoutInflater = fakeLayoutInflater()

        var called = 0
        var viewHolder: AdapterDelegateViewBindingViewHolder<Any, ViewBinding>? = null
        val binding = ViewBinding {
            view
        }
        val delegate =
            adapterDelegateViewBinding<Any, Any, ViewBinding>(
                viewBinding = { _, _ -> binding },
                layoutInflater = { layoutInflater }
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
        val view = fakeView()
        val viewGroup = fakeViewGroup()
        val layoutInflater = fakeLayoutInflater()

        val binding = ViewBinding {
            view
        }
        try {
            val delegate =
                adapterDelegateViewBinding<Any, Any, ViewBinding>(
                    viewBinding = { _, _ -> binding },
                    layoutInflater = { layoutInflater }
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
        val view = fakeView()
        val viewGroup = fakeViewGroup()
        val layoutInflater = fakeLayoutInflater()

        var called = 0
        var viewHolder: AdapterDelegateViewBindingViewHolder<Any, ViewBinding>? = null
        val binding = ViewBinding {
            view
        }
        val delegate =
            adapterDelegateViewBinding<Any, Any, ViewBinding>(
                viewBinding = { _, _ -> binding },
                layoutInflater = { layoutInflater }
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
        val view = fakeView()
        val viewGroup = fakeViewGroup()
        val layoutInflater = fakeLayoutInflater()

        val binding = ViewBinding {
            view
        }
        try {
            val delegate =
                adapterDelegateViewBinding<Any, Any, ViewBinding>(
                    viewBinding = { _, _ -> binding },
                    layoutInflater = { layoutInflater }
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
        val view = fakeView()
        val viewGroup = fakeViewGroup()
        val layoutInflater = fakeLayoutInflater()

        var called = 0
        var viewHolder: AdapterDelegateViewBindingViewHolder<Any, ViewBinding>? = null
        val binding = ViewBinding {
            view
        }
        val delegate =
            adapterDelegateViewBinding<Any, Any, ViewBinding>(
                viewBinding = { _, _ -> binding },
                layoutInflater = { layoutInflater }
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
        val view = fakeView()
        val viewGroup = fakeViewGroup()
        val layoutInflater = fakeLayoutInflater()

        val binding = ViewBinding {
            view
        }
        try {
            val delegate =
                adapterDelegateViewBinding<Any, Any, ViewBinding>(
                    viewBinding = { _, _ -> binding },
                    layoutInflater = { layoutInflater }
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