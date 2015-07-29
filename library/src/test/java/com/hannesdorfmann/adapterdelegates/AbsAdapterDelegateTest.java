/*
 * Copyright (c) 2015 Hannes Dorfmann.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hannesdorfmann.adapterdelegates;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import junit.framework.Assert;
import org.junit.Test;

/**
 * @author Hannes Dorfmann
 */
public class AbsAdapterDelegateTest {

  @Test public void testAbsAdapterDelegate() throws Exception {

    int viewType = 0;

    AbsAdapterDelegate d = new AbsAdapterDelegate(viewType) {
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

    Assert.assertSame(viewType, d.getItemViewType());


  }
}
