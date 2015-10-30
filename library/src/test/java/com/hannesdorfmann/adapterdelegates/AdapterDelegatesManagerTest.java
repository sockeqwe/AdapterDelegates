package com.hannesdorfmann.adapterdelegates;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 * @author Hannes Dorfmann
 */
public class AdapterDelegatesManagerTest {

  @Test public void testAddRemove() {

    int viewType = 0;

    AdapterDelegate d1 = new AbsAdapterDelegate(viewType) {
      @Override public boolean isForViewType(Object items, int position) {
        return false;
      }

      @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return null;
      }

      @Override
      public void onBindViewHolder(Object items, int position, RecyclerView.ViewHolder holder) {

      }
    };

    AdapterDelegate d2 = new AbsAdapterDelegate(viewType) {
      @Override public boolean isForViewType(Object items, int position) {
        return false;
      }

      @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return null;
      }

      @Override
      public void onBindViewHolder(Object items, int position, RecyclerView.ViewHolder holder) {

      }
    };

    AdapterDelegatesManager manager = new AdapterDelegatesManager();
    manager.addDelegate(d1);

    Assert.assertTrue(manager.delegates.get(viewType) == d1);

    try {
      // replacing no allowed
      manager.addDelegate(d2);
      Assert.fail("Replacing delegate should fail");
    } catch (IllegalArgumentException e) {
      Assert.assertTrue(manager.delegates.get(viewType) == d1);
    }

    // replacing allowed
    manager.addDelegate(d2, true);
    Assert.assertTrue(manager.delegates.get(viewType) == d2);

    // Remove a delegate should have no impact, because its already remmoved
    manager.removeDelegate(d1);
    Assert.assertTrue(manager.delegates.get(viewType) == d2);

    // Should remove d2
    manager.removeDelegate(viewType);
    Assert.assertNull(manager.delegates.get(viewType));
  }

  @Test public void testIsForViewType() {

    // 3 elements and each element has it's own viewtype and hence own delegate
    List<Object> items = Arrays.asList(new Object(), new Object(), new Object());
    SpyableAdapterDelegate<List<Object>> d0 = new SpyableAdapterDelegate<>(0);
    SpyableAdapterDelegate<List<Object>> d1 = new SpyableAdapterDelegate<>(1);
    SpyableAdapterDelegate<List<Object>> d2 = new SpyableAdapterDelegate<>(2);

    AdapterDelegatesManager<List<Object>> manager = new AdapterDelegatesManager<>();
    manager.addDelegate(d0);
    manager.addDelegate(d1);
    manager.addDelegate(d2);

    // Test first item
    int viewType = manager.getItemViewType(items, 0);
    Assert.assertEquals(viewType, 0);
    Assert.assertTrue(d0.isForViewTypeReturnedYes);
    Assert.assertFalse(d1.isForViewTypeReturnedYes);
    Assert.assertFalse(d2.isForViewTypeReturnedYes);
    resetDelegates(d0, d1, d2);

    // Test second item
    viewType = manager.getItemViewType(items, 1);
    Assert.assertEquals(viewType, 1);
    Assert.assertTrue(d1.isForViewTypeReturnedYes);
    Assert.assertFalse(d0.isForViewTypeReturnedYes);
    Assert.assertFalse(d2.isForViewTypeReturnedYes);
    resetDelegates(d0, d1, d2);

    // Test third item
    viewType = manager.getItemViewType(items, 2);
    Assert.assertEquals(viewType, 2);
    Assert.assertTrue(d2.isForViewTypeReturnedYes);
    Assert.assertFalse(d0.isForViewTypeReturnedYes);
    Assert.assertFalse(d1.isForViewTypeReturnedYes);
    resetDelegates(d0, d1, d2);
  }

  @Test public void testOnCreateViewHolder() {

    // 3 elements and each element has it's own viewtype and hence own delegate
    List<Object> items = Arrays.asList(new Object(), new Object(), new Object());
    SpyableAdapterDelegate<List<Object>> d0 = new SpyableAdapterDelegate<>(0);
    SpyableAdapterDelegate<List<Object>> d1 = new SpyableAdapterDelegate<>(1);
    SpyableAdapterDelegate<List<Object>> d2 = new SpyableAdapterDelegate<>(2);

    AdapterDelegatesManager<List<Object>> manager = new AdapterDelegatesManager<>();
    manager.addDelegate(d0);
    manager.addDelegate(d1);
    manager.addDelegate(d2);

    // Test first item
    RecyclerView.ViewHolder vh = manager.onCreateViewHolder(null, 0);
    Assert.assertSame(vh, d0.viewHolder);
    Assert.assertTrue(d0.onCreateViewHolderCalled);
    Assert.assertFalse(d1.onCreateViewHolderCalled);
    Assert.assertFalse(d2.onCreateViewHolderCalled);

    resetDelegates(d0, d1, d2);

    // Test second item
    vh = manager.onCreateViewHolder(null, 1);
    Assert.assertSame(vh, d1.viewHolder);
    Assert.assertTrue(d1.onCreateViewHolderCalled);
    Assert.assertFalse(d0.onCreateViewHolderCalled);
    Assert.assertFalse(d2.onCreateViewHolderCalled);

    resetDelegates(d0, d1, d2);

    // Test second item
    vh = manager.onCreateViewHolder(null, 2);
    Assert.assertSame(vh, d2.viewHolder);
    Assert.assertTrue(d2.onCreateViewHolderCalled);
    Assert.assertFalse(d0.onCreateViewHolderCalled);
    Assert.assertFalse(d1.onCreateViewHolderCalled);
  }

  @Test public void testOnBindViewHolder() {

    // 3 elements and each element has it's own viewtype and hence own delegate
    List<Object> items = Arrays.asList(new Object(), new Object(), new Object());
    SpyableAdapterDelegate<List<Object>> d0 = new SpyableAdapterDelegate<>(0);
    SpyableAdapterDelegate<List<Object>> d1 = new SpyableAdapterDelegate<>(1);
    SpyableAdapterDelegate<List<Object>> d2 = new SpyableAdapterDelegate<>(2);

    AdapterDelegatesManager<List<Object>> manager = new AdapterDelegatesManager<>();
    manager.addDelegate(d0);
    manager.addDelegate(d1);
    manager.addDelegate(d2);

    // Test first item
    manager.onBindViewHolder(items, 0, d0.viewHolder);
    Assert.assertTrue(d0.onBindViewHolderCalled);
    Assert.assertFalse(d1.onBindViewHolderCalled);
    Assert.assertFalse(d2.onBindViewHolderCalled);

    resetDelegates(d0, d1, d2);

    // Test second item
    manager.onBindViewHolder(items, 1, d1.viewHolder);
    Assert.assertTrue(d1.onBindViewHolderCalled);
    Assert.assertFalse(d0.onBindViewHolderCalled);
    Assert.assertFalse(d2.onBindViewHolderCalled);

    resetDelegates(d0, d1, d2);

    // Test third item
    manager.onBindViewHolder(items, 2, d2.viewHolder);
    Assert.assertTrue(d2.onBindViewHolderCalled);
    Assert.assertFalse(d1.onBindViewHolderCalled);
    Assert.assertFalse(d0.onBindViewHolderCalled);

    resetDelegates(d0, d1, d2);
  }

  @Test public void allMethodsTest() {

    // 3 elements and each element has it's own viewtype and hence own delegate
    List<Object> items = Arrays.asList(new Object(), new Object(), new Object());
    SpyableAdapterDelegate<List<Object>> d0 = new SpyableAdapterDelegate<>(0);
    SpyableAdapterDelegate<List<Object>> d1 = new SpyableAdapterDelegate<>(1);
    SpyableAdapterDelegate<List<Object>> d2 = new SpyableAdapterDelegate<>(2);

    AdapterDelegatesManager<List<Object>> manager = new AdapterDelegatesManager<>();
    manager.addDelegate(d0);
    manager.addDelegate(d1);
    manager.addDelegate(d2);

    SpyableAdapterDelegate<List<Object>>[] delegates = new SpyableAdapterDelegate[] {
        d0, d1, d2
    };

    for (int i = 0; i < items.size(); i++) {
      SpyableAdapterDelegate<List<Object>> expectedDelegate = delegates[i];

      int viewType = manager.getItemViewType(items, i);

      // Test view type
      Assert.assertEquals(viewType, expectedDelegate.viewType);
      for (SpyableAdapterDelegate d : delegates) {
        if (d == expectedDelegate) {
          Assert.assertTrue(d.isForViewTypeReturnedYes);
        } else {
          Assert.assertFalse(d.isForViewTypeReturnedYes);
        }
      }

      // Test create viewHolder
      RecyclerView.ViewHolder vh = manager.onCreateViewHolder(null, viewType);
      Assert.assertSame(vh, expectedDelegate.viewHolder);

      for (SpyableAdapterDelegate d : delegates) {
        if (d == expectedDelegate) {
          Assert.assertTrue(d.onCreateViewHolderCalled);
        } else {
          Assert.assertFalse(d.onCreateViewHolderCalled);
        }
      }

      // Test bind viewHolder
      manager.onBindViewHolder(items, i, vh);
      for (SpyableAdapterDelegate d : delegates) {
        if (d == expectedDelegate) {
          Assert.assertTrue(d.onBindViewHolderCalled);
        } else {
          Assert.assertFalse(d.onBindViewHolderCalled);
        }
      }

      resetDelegates(delegates);
    }
  }

  private void resetDelegates(SpyableAdapterDelegate<?>... delegates) {
    for (SpyableAdapterDelegate d : delegates) {
      d.reset();
    }
  }

  @Test(expected = NullPointerException.class) public void testNoDelegates() {
    AdapterDelegatesManager<Object> delegatesManager = new AdapterDelegatesManager<>();
    delegatesManager.onCreateViewHolder(null, 1); // No Delegate --> throw Exception
  }

  @Test public void testUnknownDelegate() {
    AdapterDelegatesManager<Object> delegatesManager = new AdapterDelegatesManager<>();
    delegatesManager.addDelegate(new SpyableAdapterDelegate<Object>(0));

    delegatesManager.onCreateViewHolder(null, 0); // NO exception
    try {
      delegatesManager.onCreateViewHolder(null,
          1); // There is no delegates manager for ViewType == 1,
      Assert.fail(
          "Exception should be thrown because no Delegate for given ViewType is registered");
    } catch (NullPointerException e) {
      // No delegate for view type 1 registered --> Nullpointer exception will be thrown
    }
  }

  @Test public void testFallbackUnknownDelegate() {

    int fallbackViewType = Integer.MAX_VALUE - 1;
    int itemPosition = 1;
    List<Object> items = new ArrayList<>();
    items.add(new Object());

    AdapterDelegatesManager<List<Object>> delegatesManager = new AdapterDelegatesManager<>();
    SpyableAdapterDelegate fallbackDelegate = new SpyableAdapterDelegate(fallbackViewType);

    SpyableAdapterDelegate<List<Object>> otherDelegate = new SpyableAdapterDelegate<>(0);
    delegatesManager.setFallbackDelegate(fallbackDelegate);
    delegatesManager.addDelegate(otherDelegate);

    RecyclerView.ViewHolder vh = delegatesManager.onCreateViewHolder(null,
        fallbackViewType); // There is no delegates manager for ViewType == 1

    Assert.assertSame(vh, fallbackDelegate.viewHolder);
    Assert.assertTrue(fallbackDelegate.onCreateViewHolderCalled);
    Assert.assertFalse(otherDelegate.onCreateViewHolderCalled);

    // Test bind viewHolder
    delegatesManager.onBindViewHolder(items, itemPosition, vh);
    Assert.assertTrue(fallbackDelegate.onBindViewHolderCalled);
    Assert.assertFalse(otherDelegate.onBindViewHolderCalled);
  }

  @Test public void testFallbackViewTypeConflictsDelegateViewType() {

    int viewType = 2;

    AdapterDelegatesManager<List> manager1 = new AdapterDelegatesManager<>();
    AdapterDelegatesManager<List> manager2 = new AdapterDelegatesManager<>();

    // Both have the same view type
    SpyableAdapterDelegate<List> fallback = new SpyableAdapterDelegate<>(viewType);
    SpyableAdapterDelegate<List> delegate = new SpyableAdapterDelegate<>(viewType);
    manager1.setFallbackDelegate(fallback);

    try {
      manager1.addDelegate(delegate);
      Assert.fail(
          "An excepetion should be thrown because AdapterDelegate conflicts with fallback ViewType");
    } catch (IllegalArgumentException e) {
      // Excepted exception
    }

    manager2.addDelegate(delegate);
    try {
      manager2.setFallbackDelegate(fallback);
      Assert.fail(
          "An exception should be thrown because fallback conflicts with AdapterDelegates ViewType");
    } catch (IllegalArgumentException exception) {
      // Expected Exception
    }
  }
}
