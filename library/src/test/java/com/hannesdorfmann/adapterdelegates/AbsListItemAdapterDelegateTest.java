package com.hannesdorfmann.adapterdelegates;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class AbsListItemAdapterDelegateTest {

  @Test public void invokeMethods() {

    List<Animal> items = new ArrayList<>();
    items.add(new Cat());

    CatAbsListItemAdapterDelegate delegate = new CatAbsListItemAdapterDelegate();

    delegate.isForViewType(items, 0);
    Assert.assertTrue(delegate.isForViewTypeCalled);

    ViewGroup parent = Mockito.mock(ViewGroup.class);
    CatViewHolder vh = delegate.onCreateViewHolder(parent);
    Assert.assertTrue(delegate.onCreateViewHolderCalled);

    delegate.onBindViewHolder(items, 0, vh);
    Assert.assertTrue(delegate.onBindViewHolderCalled);
  }

  interface Animal {
  }

  class Cat implements Animal {
  }

  class CatViewHolder extends RecyclerView.ViewHolder {
    public CatViewHolder(View itemView) {
      super(itemView);
    }
  }

  class CatAbsListItemAdapterDelegate
      extends AbsListItemAdapterDelegate<Cat, Animal, CatViewHolder> {
    public boolean isForViewTypeCalled = false;
    public boolean onCreateViewHolderCalled = false;
    public boolean onBindViewHolderCalled = false;

    public CatAbsListItemAdapterDelegate() {
      super(0);
    }

    @Override
    protected boolean isForViewType(@NonNull Cat item, List<Animal> items, int position) {
      isForViewTypeCalled = true;
      return false;
    }

    @NonNull @Override public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
      onCreateViewHolderCalled = true;
      return new CatViewHolder(Mockito.mock(View.class));
    }

    @Override
    protected void onBindViewHolder(@NonNull Cat item, @NonNull CatViewHolder viewHolder) {
      onBindViewHolderCalled = true;
    }
  }
}
