package com.hannesdorfmann.adapterdelegates.sample.adapterdelegates;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hannesdorfmann.adapterdelegates.AbsListItemAdapterDelegate;
import com.hannesdorfmann.adapterdelegates.sample.R;
import com.hannesdorfmann.adapterdelegates.sample.model.DisplayableItem;
import com.hannesdorfmann.adapterdelegates.sample.model.Snake;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class SnakeListItemAdapterDelegate extends
    AbsListItemAdapterDelegate<Snake, DisplayableItem, SnakeListItemAdapterDelegate.SnakeViewHolder> {

  private LayoutInflater inflater;

  public SnakeListItemAdapterDelegate(Activity activity, int viewType) {
    super(viewType);
    inflater = activity.getLayoutInflater();
  }

  @Override
  protected boolean isForViewType(@NonNull Snake item, List<DisplayableItem> items, int position) {
    return item instanceof Snake;
  }

  @NonNull @Override
  public SnakeListItemAdapterDelegate.SnakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
    return new SnakeListItemAdapterDelegate.SnakeViewHolder(
        inflater.inflate(R.layout.item_snake, parent, false));
  }

  @Override protected void onBindViewHolder(@NonNull Snake snake,
      @NonNull SnakeListItemAdapterDelegate.SnakeViewHolder vh) {

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
