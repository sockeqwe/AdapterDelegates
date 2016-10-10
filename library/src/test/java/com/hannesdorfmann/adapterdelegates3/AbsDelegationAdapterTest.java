package com.hannesdorfmann.adapterdelegates3;

import android.view.ViewGroup;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Hannes Dorfmann
 */
public class AbsDelegationAdapterTest {

  @Test public void delegatesManagerNull() {
    try {
      AbsDelegationAdapter<Object> adapter = new AbsDelegationAdapter<Object>(null) {
        @Override public int getItemCount() {
          return 0;
        }
      };
      Assert.fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      Assert.assertEquals("AdapterDelegatesManager is null", e.getMessage());
    }
  }

  @Test public void checkDelegatesManagerInstance() {

    final AdapterDelegatesManager<Object> manager = new AdapterDelegatesManager<>();

    AbsDelegationAdapter<Object> adapter = new AbsDelegationAdapter<Object>(manager) {
      @Override public int getItemCount() {
        // Hacky but does the job
        Assert.assertTrue(manager == this.delegatesManager);
        return 0;
      }
    };

    adapter.getItemCount();
  }

  @Test public void checkNewAdapterDelegatesManagerInstanceNotNull() {

    // Empty constructor should produce a new instance of AdapterDelegatesManager
    AbsDelegationAdapter<Object> adapter = new AbsDelegationAdapter<Object>() {
      @Override public int getItemCount() {
        // Hacky but does the job
        Assert.assertNotNull(this.delegatesManager);
        return 0;
      }
    };

    adapter.getItemCount();
  }

  @Test public void callAllMethods() {

    SpyableAdapterDelegate<Object> delegate1 = new SpyableAdapterDelegate<Object>(0);
    SpyableAdapterDelegate<Object> delegate2 = new SpyableAdapterDelegate<Object>(1);
    AdapterDelegatesManager<Object> manager =
        new AdapterDelegatesManager<>()
            .addDelegate(delegate1)
            .addDelegate(delegate2);

    AbsDelegationAdapter<Object> adapter = new AbsDelegationAdapter<Object>(manager) {
      @Override public int getItemCount() {
        return 1;
      }
    };

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
