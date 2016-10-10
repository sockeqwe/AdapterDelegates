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

package com.hannesdorfmann.adapterdelegates3.sample.adapterdelegates;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hannesdorfmann.adapterdelegates.sample.R;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.hannesdorfmann.adapterdelegates3.sample.model.DisplayableItem;
import com.hannesdorfmann.adapterdelegates3.sample.model.Dog;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class DogAdapterDelegate extends AdapterDelegate<List<DisplayableItem>> {

  private LayoutInflater inflater;

  public DogAdapterDelegate(Activity activity) {
    inflater = activity.getLayoutInflater();
  }

  @Override public boolean isForViewType(@NonNull List<DisplayableItem> items, int position) {
    return items.get(position) instanceof Dog;
  }

  @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {

    Log.d("Scroll", "DogAdapterDelegate create");
    return new DogViewHolder(inflater.inflate(R.layout.item_dog, parent, false));
  }

  @Override public void onBindViewHolder(@NonNull List<DisplayableItem> items, int position,
      @NonNull RecyclerView.ViewHolder holder, @Nullable List<Object> payloads) {
    DogViewHolder vh = (DogViewHolder) holder;
    Dog dog = (Dog) items.get(position);

    vh.name.setText(dog.getName());

    Log.d("Scroll", "DogAdapterDelegate bind  " + position);
  }

  static class DogViewHolder extends RecyclerView.ViewHolder {

    public TextView name;

    public DogViewHolder(View itemView) {
      super(itemView);
      name = (TextView) itemView.findViewById(R.id.name);
    }
  }
}
