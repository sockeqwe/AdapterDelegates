package com.hannesdorfmann.adapterdelegates4;

import android.view.ViewGroup;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;

/**
 * @author Hannes Dorfmann
 */
public class AsyncListDifferDelegationAdapterTest {

    private final DiffUtil.ItemCallback<Object> callback = new DiffUtil.ItemCallback<Object>() {
        @Override
        public boolean areItemsTheSame(Object oldItem, Object newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(Object oldItem, Object newItem) {
            return false;
        }
    };

    @Test
    public void itemCallbackIsNull() {
        try {
            AsyncListDifferDelegationAdapter<Object> adapter = new AsyncListDifferDelegationAdapter<Object>(null) {
                @Override
                public int getItemCount() {
                    return 0;
                }
            };
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            Assert.assertEquals("ItemCallback is null", e.getMessage());
        }
    }

    @Test
    public void adapterDelegateManagerIsNull() {
        try {
            AsyncListDifferDelegationAdapter<Object> adapter = new AsyncListDifferDelegationAdapter<Object>(callback, null) {
                @Override
                public int getItemCount() {
                    return 0;
                }
            };
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            Assert.assertEquals("AdapterDelegatesManager is null", e.getMessage());
        }
    }

    @Test
    @Ignore("Why does Mockito can't mock the final class anymore?")
    public void checkDelegatesManagerInstance() {

        final AdapterDelegatesManager<List<Object>> manager = new AdapterDelegatesManager<>();
        AsyncDifferConfig<Object> config = Mockito.mock(AsyncDifferConfig.class);
        AsyncListDifferDelegationAdapter<Object> adapter = new AsyncListDifferDelegationAdapter<Object>(config, manager) {
            @Override
            public int getItemCount() {
                // Hacky but does the job
                Assert.assertTrue(manager == this.delegatesManager);
                return 0;
            }
        };

        adapter.getItemCount();
    }

    @Test
    @Ignore("Why does Mockito can't mock the final class anymore?")
    public void callAllMethods() {

        final SpyableAdapterDelegate<List<Object>> delegate1 = new SpyableAdapterDelegate<>(0);
        final SpyableAdapterDelegate<List<Object>> delegate2 = new SpyableAdapterDelegate<>(1);

        final AdapterDelegatesManager<List<Object>> manager =
                new AdapterDelegatesManager<List<Object>>()
                        .addDelegate(delegate1)
                        .addDelegate(delegate2);

        AsyncDifferConfig<Object> config = Mockito.mock(AsyncDifferConfig.class);

        AsyncListDifferDelegationAdapter<Object> adapter = new AsyncListDifferDelegationAdapter<Object>(config, manager);

        ViewGroup parent = Mockito.mock(ViewGroup.class);


        // CreateViewHolder
        adapter.onCreateViewHolder(parent, 0);
        Assert.assertTrue(delegate1.onCreateViewHolderCalled);
        Assert.assertFalse(delegate2.onCreateViewHolderCalled);

        // BindViewHolder
        adapter.onBindViewHolder(delegate1.viewHolder, 1);
        Assert.assertTrue(delegate1.onBindViewHolderCalled);
        Assert.assertFalse(delegate2.onBindViewHolderCalled);


        // bind with payload
        delegate1.onBindViewHolderCalled = false; // reset
        adapter.onBindViewHolder(delegate1.viewHolder, 1, Collections.emptyList());
        Assert.assertTrue(delegate1.onBindViewHolderCalled);
        Assert.assertFalse(delegate2.onBindViewHolderCalled);


        // On view AttachedToWindow
        adapter.onViewAttachedToWindow(delegate1.viewHolder);
        Assert.assertTrue(delegate1.onViewAtachedToWindowCalled);
        Assert.assertFalse(delegate2.onViewAtachedToWindowCalled);

        // On view Detached from window
        adapter.onViewDetachedFromWindow(delegate1.viewHolder);
        Assert.assertTrue(delegate1.onViewDetachedFromWindowCalled);
        Assert.assertFalse(delegate2.onViewDetachedFromWindowCalled);

        // failed to recycle view holder
        Assert.assertFalse(adapter.onFailedToRecycleView(delegate1.viewHolder));
        Assert.assertTrue(delegate1.onFailedToRecycleViewCalled);
        Assert.assertFalse(delegate2.onFailedToRecycleViewCalled);


        // view  recycle view holder
        adapter.onViewRecycled(delegate1.viewHolder);
        Assert.assertTrue(delegate1.onViewRecycledCalled);
        Assert.assertFalse(delegate2.onViewRecycledCalled);
    }
}
