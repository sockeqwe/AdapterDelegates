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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * This class is the element that ties {@link RecyclerView.Adapter} together with {@link
 * AdapterDelegate}.
 * <p>
 * So you have to add / register your {@link AdapterDelegate}s to this manager by calling {@link
 * #addDelegate(AdapterDelegate)}
 * </p>
 *
 * <p>
 * Next you have to add this AdapterDelegatesManager to the {@link RecyclerView.Adapter} by calling
 * corresponding methods:
 * <ul>
 * <li> {@link #getItemViewType(Object, int)}: Must be called from {@link
 * RecyclerView.Adapter#getItemViewType(int)}</li>
 * <li> {@link #onCreateViewHolder(ViewGroup, int)}: Must be called from {@link
 * RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)}</li>
 * <li> {@link #onBindViewHolder(Object, int, RecyclerView.ViewHolder)}: Must be called from {@link
 * RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}</li>
 * </ul>
 *
 * You can also set a fallback {@link AdapterDelegate} by using {@link
 * #setFallbackDelegate(AdapterDelegate)} that will be used if no {@link AdapterDelegate} is
 * responsible to handle a certain view type. If no fallback is specified, an Exception will be
 * thrown if no {@link AdapterDelegate} is responsible to handle a certain view type
 * </p>
 *
 * @param <T> The type of the datasource of the adapter
 * @author Hannes Dorfmann
 */
public class AdapterDelegatesManager<T> {

  /**
   * Map for ViewType to AdapterDeleage
   */
  SparseArrayCompat<AdapterDelegate<T>> delegates = new SparseArrayCompat();
  private AdapterDelegate<T> fallbackDelegate;

  /**
   * Adds an {@link AdapterDelegate}. Internally calls {@link #addDelegate(AdapterDelegate,
   * boolean)} with false as parameter.
   *
   * @param delegate the delegate to add
   * @return self
   * @throws IllegalArgumentException if an {@link AdapterDelegate} is already added (registered)
   * with the same ViewType {@link AdapterDelegate#getItemViewType()}.
   * @see #addDelegate(AdapterDelegate, boolean)
   */
  public AdapterDelegatesManager<T> addDelegate(@NonNull AdapterDelegate<T> delegate) {
    return addDelegate(delegate, false);
  }

  /**
   * Adds an {@link AdapterDelegate}.
   *
   * @param delegate The delegate to add
   * @param allowReplacingDelegate if true, you allow to replacing the given delegate any previous
   * delegate for the same view type. if false, you disallow and a {@link IllegalArgumentException}
   * will be thrown if you try to replace an already registered {@link AdapterDelegate} for the
   * same
   * view type.
   * @throws IllegalArgumentException if <b>allowReplacingDelegate</b>  is false and an {@link
   * AdapterDelegate} is already added (registered)
   * with the same ViewType {@link AdapterDelegate#getItemViewType()}.
   * @throws IllegalArgumentException if the {@link AdapterDelegate#getItemViewType()} is the same
   * as fallback AdapterDelegate one.
   * @see #setFallbackDelegate(AdapterDelegate)
   */
  public AdapterDelegatesManager<T> addDelegate(@NonNull AdapterDelegate<T> delegate,
      boolean allowReplacingDelegate) {

    if (delegate == null) {
      throw new NullPointerException("AdapterDelegate is null!");
    }

    int viewType = delegate.getItemViewType();

    if (fallbackDelegate != null && fallbackDelegate.getItemViewType() == viewType) {
      throw new IllegalArgumentException(
          "Conflict: the passed AdapterDelegate has the same ViewType integer (value = " + viewType
              + ") as the fallback AdapterDelegate");
    }
    if (!allowReplacingDelegate && delegates.get(viewType) != null) {
      throw new IllegalArgumentException(
          "An AdapterDelegate is already registered for the viewType = " + viewType
              + ". Already registered AdapterDelegate is " + delegates.get(viewType));
    }

    delegates.put(viewType, delegate);

    return this;
  }

  /**
   * Removes a previously registered delegate if and only if the passed delegate is registered
   * (checks the reference of the object). This will not remove any other delegate for the same
   * viewType (if there is any).
   *
   * @param delegate The delegate to remove
   * @return self
   */
  public AdapterDelegatesManager<T> removeDelegate(@NonNull AdapterDelegate<T> delegate) {

    if (delegate == null) {
      throw new NullPointerException("AdapterDelegate is null");
    }

    AdapterDelegate<T> queried = delegates.get(delegate.getItemViewType());
    if (queried != null && queried == delegate) {
      delegates.remove(delegate.getItemViewType());
    }
    return this;
  }

  /**
   * Removes the adapterDelegate for the given view types.
   *
   * @param viewType The Viewtype
   * @return self
   */
  public AdapterDelegatesManager<T> removeDelegate(int viewType) {
    delegates.remove(viewType);
    return this;
  }

  /**
   * Must be called from {@link RecyclerView.Adapter#getItemViewType(int)}. Internally it scans all
   * the registered {@link AdapterDelegate} and picks the right one to return the ViewType integer.
   *
   * @param items Adapter's data source
   * @param position the position in adapters data source
   * @return the ViewType (integer)
   * @throws IllegalArgumentException if no {@link AdapterDelegate} has been found that is
   * responsible for the given data element in data set (No {@link AdapterDelegate} for the given
   * ViewType)
   * @throws NullPointerException if items is null
   */
  public int getItemViewType(@NonNull T items, int position) {

    if (items == null) {
      throw new NullPointerException("Items datasource is null!");
    }

    int delegatesCount = delegates.size();
    for (int i = 0; i < delegatesCount; i++) {
      AdapterDelegate<T> delegate = delegates.valueAt(i);
      if (delegate.isForViewType(items, position)) {
        return delegate.getItemViewType();
      }
    }

    if (fallbackDelegate != null) {
      return fallbackDelegate.getItemViewType();
    }

    throw new IllegalArgumentException(
        "No AdapterDelegate added that matches position=" + position + " in data source");
  }

  /**
   * This method must be called in {@link RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)}
   *
   * @param parent the parent
   * @param viewType the view type
   * @return The new created ViewHolder
   * @throws NullPointerException if no AdapterDelegate has been registered for ViewHolders
   * viewType
   */
  @NonNull public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    AdapterDelegate<T> delegate = delegates.get(viewType);
    if (delegate == null) {
      if (fallbackDelegate == null) {
        throw new NullPointerException("No AdapterDelegate added for ViewType " + viewType);
      } else {
        delegate = fallbackDelegate;
      }
    }

    RecyclerView.ViewHolder vh = delegate.onCreateViewHolder(parent);
    if (vh == null) {
      throw new NullPointerException(
          "ViewHolder returned from AdapterDelegate " + delegate + " for ViewType =" + viewType
              + " is null!");
    }
    return vh;
  }

  /**
   * Must be called from{@link RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}
   *
   * @param items Adapter's data source
   * @param position the position in data source
   * @param viewHolder the ViewHolder to bind
   * @throws NullPointerException if no AdapterDelegate has been registered for ViewHolders
   * viewType
   */
  public void onBindViewHolder(@NonNull T items, int position,
      @NonNull RecyclerView.ViewHolder viewHolder) {

    AdapterDelegate<T> delegate = delegates.get(viewHolder.getItemViewType());
    if (delegate == null) {
      if (fallbackDelegate == null) {
        throw new NullPointerException(
            "No AdapterDelegate added for ViewType " + viewHolder.getItemViewType());
      } else {
        delegate = fallbackDelegate;
      }
    }

    delegate.onBindViewHolder(items, position, viewHolder);
  }

  /**
   * Set a fallback delegate that should be used if no {@link AdapterDelegate} has been found that
   * can handle a certain view type.
   *
   * @param fallbackDelegate The {@link AdapterDelegate} that should be used as fallback if no
   * other
   * AdapterDelegate has handled a certain view type. <code>null</code> you can set this to null if
   * you want to remove a previously set fallback AdapterDelegate
   * @throws IllegalArgumentException If passed Fallback
   */
  public AdapterDelegatesManager<T> setFallbackDelegate(
      @Nullable AdapterDelegate<T> fallbackDelegate) {

    if (fallbackDelegate != null) {
      // Setting a new fallback delegate
      int delegatesCount = delegates.size();
      int fallbackViewType = fallbackDelegate.getItemViewType();
      for (int i = 0; i < delegatesCount; i++) {
        AdapterDelegate<T> delegate = delegates.valueAt(i);
        if (delegate.getItemViewType() == fallbackViewType) {
          throw new IllegalArgumentException(
              "Conflict: The given fallback - delegate has the same ViewType integer (value = "
                  + fallbackViewType + ")  as an already assigned AdapterDelegate "
                  + delegate.getClass().getName());
        }
      }
    }
    this.fallbackDelegate = fallbackDelegate;

    return this;
  }
}
