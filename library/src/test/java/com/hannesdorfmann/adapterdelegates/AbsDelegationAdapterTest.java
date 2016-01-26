package com.hannesdorfmann.adapterdelegates;

import org.junit.Assert;
import org.junit.Test;

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
}
