package com.hannesdorfmann.adapterdelegates2;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class SpyableAdapterDelegate<T> implements AdapterDelegate<T> {

  public boolean isForViewTypeReturnedYes = false;
  public boolean onCreateViewHolderCalled = false;
  public boolean onBindViewHolderCalled = false;

  public int onBindViewHolderPosition = -1;
  public RecyclerView.ViewHolder viewHolder;
  public int viewType;

  public SpyableAdapterDelegate(int viewType) {
    this.viewType = viewType;
    viewHolder = new RecyclerView.ViewHolder(new View(null)) {
    };

    try {
      Field viewTypeField = RecyclerView.ViewHolder.class.
          getDeclaredField("mItemViewType");

      viewTypeField.setAccessible(true);
      viewTypeField.set(viewHolder, viewType);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void reset() {
    isForViewTypeReturnedYes = false;
    onCreateViewHolderCalled = false;
    onBindViewHolderCalled = false;
    onBindViewHolderPosition = -1;
  }

  @Override public boolean isForViewType(T items, int position) {

    boolean isForThat = position == viewType;
    if (isForThat) {
      isForViewTypeReturnedYes = true;
    }
    return isForThat;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {

    onCreateViewHolderCalled = true;
    return viewHolder;
  }

	@Override
	public void onBindViewHolder(@NonNull T items, int position, @NonNull RecyclerView.ViewHolder holder, @Nullable List payloads) {
		onBindViewHolderCalled = true;
		onBindViewHolderPosition = position;
	}

}
