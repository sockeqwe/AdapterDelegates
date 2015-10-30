package com.hannesdorfmann.adapterdelegates;

import android.support.annotation.NonNull;

/**
 * This class can be used as base class for a fallback delegate {@link
 * AdapterDelegatesManager#setFallbackDelegate(AdapterDelegate)}. Internally it sets the ViewType
 * integer to {@code Integer.MAX_VALUE -1} which should avoid conflicts with other {@link
 * AdapterDelegate}s.
 *
 * @author Hannes Dorfmann
 * @since 1.1.0
 */
public abstract class AbsFallbackAdapterDelegate<T> extends AbsAdapterDelegate<T> {

  public AbsFallbackAdapterDelegate() {
    super(Integer.MAX_VALUE - 1);
  }

  /**
   * Not really needed.
   *
   * @param items The data source of the Adapter
   * @param position The position in the datasource
   * @return true
   */
  @Override public boolean isForViewType(@NonNull Object items, int position) {
    return true;
  }
}
