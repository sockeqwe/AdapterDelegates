package com.hannesdorfmann.adapterdelegates3;

import android.support.v7.recyclerview.extensions.AsyncListDiffer;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by six_hundreds on 28.06.18.
 */

public class DiffDelegationAdapterTest {

    @Test
    public void checkDelegatesManagerNotNull() {
        new DiffDelegationAdapter() {
            @Override
            public int getItemCount() {
                Assert.assertNotNull(delegatesManager);
                return 0;
            }
        };

    }

    @Test
    public void checkDiffCallbackNotNull() {
        new DiffDelegationAdapter() {
            @Override
            public int getItemCount() {
                Assert.assertNotNull(differ);
                return 0;
            }
        };
    }

    @Test
    public void checkDiffCallbackInstance() {
        final DiffCallbackImpl<DiffItem> callback = new DiffCallbackImpl<>();
        new DiffDelegationAdapter<DiffItem>(callback) {
            @Override
            public int getItemCount() {
                AsyncListDiffer asyncListDiffer = new AsyncListDiffer<>(this, callback);
                Assert.assertEquals(differ, asyncListDiffer);
                return 0;
            }
        };
    }

}
