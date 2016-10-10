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

package com.hannesdorfmann.adapterdelegates3;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.List;

/**
 * An implementation of an Adapter that already uses a {@link AdapterDelegatesManager} and calls
 * the corresponding {@link AdapterDelegatesManager} methods from Adapter's method like {@link
 * #onCreateViewHolder(ViewGroup, int)}, {@link #onBindViewHolder(RecyclerView.ViewHolder, int)}
 * and {@link #getItemViewType(int)}. So everything is already setup for you. You just have to add
 * the {@link AdapterDelegate}s i.e. in the constructor of a subclass that inheritance from this
 * class:
 * <pre>
 * {@code
 *    class MyAdapter extends AbsDelegationAdapter<MyDataSourceType>{
 *        public MyAdaper(){
 *            this.delegatesManager.add(new FooAdapterDelegate());
 *            this.delegatesManager.add(new BarAdapterDelegate());
 *        }
 *    }
 * }
 * </pre>
 *
 * or you can pass a already prepared {@link AdapterDelegatesManager} via constructor like this:
 * <pre>
 * {@code
 *    class MyAdapter extends AbsDelegationAdapter<MyDataSourceType>{
 *        public MyAdapter(AdapterDelegatesManager manager){
 *          super(manager)
 *        }
 *    }
 * }
 * </pre>
 *
 * @param <T> The type of the datasoure / items
 * @author Hannes Dorfmann
 */
public abstract class AbsDelegationAdapter<T> extends RecyclerView.Adapter {

  protected AdapterDelegatesManager<T> delegatesManager;
  protected T items;

  public AbsDelegationAdapter() {
    this(new AdapterDelegatesManager<T>());
  }

  public AbsDelegationAdapter(@NonNull AdapterDelegatesManager<T> delegatesManager) {
    if (delegatesManager == null) {
      throw new NullPointerException("AdapterDelegatesManager is null");
    }

    this.delegatesManager = delegatesManager;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return delegatesManager.onCreateViewHolder(parent, viewType);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    delegatesManager.onBindViewHolder(items, position, holder, null);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
    delegatesManager.onBindViewHolder(items, position, holder, payloads);
  }

  @Override public int getItemViewType(int position) {
    return delegatesManager.getItemViewType(items, position);
  }

  @Override public void onViewRecycled(RecyclerView.ViewHolder holder) {
    delegatesManager.onViewRecycled(holder);
  }

  @Override public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
    return delegatesManager.onFailedToRecycleView(holder);
  }

  @Override public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
    delegatesManager.onViewAttachedToWindow(holder);
  }

  @Override public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
    delegatesManager.onViewDetachedFromWindow(holder);
  }

  /**
   * Get the items / data source of this adapter
   *
   * @return The items / data source
   */
  public T getItems() {
    return items;
  }

  /**
   * Set the items / data source of this adapter
   *
   * @param items The items / data source
   */
  public void setItems(T items) {
    this.items = items;
  }
}
