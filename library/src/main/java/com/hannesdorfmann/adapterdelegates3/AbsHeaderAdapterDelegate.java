package com.hannesdorfmann.adapterdelegates3;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public abstract class AbsHeaderAdapterDelegate<VH extends RecyclerView.ViewHolder>
		extends AdapterDelegate {

	@Override
	protected boolean isForViewType(@NonNull Object items, int position) {
		return isForViewType(position);
	}

	@Override
	protected void onBindViewHolder(@NonNull Object items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List payloads) {
		onBindViewHolder((VH) holder);
	}

	/**
	 * Called to determine whether this AdapterDelegate is the responsible for the given item in the
	 * list or not
	 * element
	 *
	 * @param position The items position in the dataset (list)
	 * @return true if this AdapterDelegate is responsible for that, otherwise false
	 */
	protected abstract boolean isForViewType(int position);

	/**
	 * Creates the  {@link RecyclerView.ViewHolder} for the given data source item
	 *
	 * @param parent The ViewGroup parent of the given datasource
	 * @return ViewHolder
	 */
	@NonNull
	@Override
	protected abstract VH onCreateViewHolder(@NonNull ViewGroup parent);

	/**
	 * Called to bind the {@link RecyclerView.ViewHolder} to the item of the dataset
	 *
	 * @param viewHolder The ViewHolder
	 */
	protected abstract void onBindViewHolder(@NonNull VH viewHolder);

}
