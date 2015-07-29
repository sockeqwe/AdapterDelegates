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

package com.hannesdorfmann.adapterdelegates.sample.adapterdelegates;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hannesdorfmann.adapterdelegates.AbsAdaterDelegate;
import com.hannesdorfmann.adapterdelegates.sample.R;
import com.hannesdorfmann.adapterdelegates.sample.model.DisplayableItem;
import com.hannesdorfmann.adapterdelegates.sample.model.Snake;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class SnakeAdapterDelegate extends AbsAdaterDelegate<List<DisplayableItem>> {

  private LayoutInflater inflater;

  public SnakeAdapterDelegate(Activity activity, int viewType) {
    super(viewType);
    inflater = activity.getLayoutInflater();
  }

  @Override public boolean isForViewType(@NonNull List<DisplayableItem> items, int position) {
    return items.get(position) instanceof Snake;
  }

  @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
    return new SnakeViewHolder(inflater.inflate(R.layout.item_snake, parent, false));
  }

  @Override public void onBindViewHolder(@NonNull List<DisplayableItem> items, int position,
      @NonNull RecyclerView.ViewHolder holder) {

    SnakeViewHolder vh = (SnakeViewHolder) holder;
    Snake snake = (Snake) items.get(position);

    vh.name.setText(snake.getName());
    vh.race.setText(snake.getRace());
  }

  static class SnakeViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView race;

    public SnakeViewHolder(View itemView) {
      super(itemView);
      name = (TextView) itemView.findViewById(R.id.name);
      race = (TextView) itemView.findViewById(R.id.race);
    }
  }
}
