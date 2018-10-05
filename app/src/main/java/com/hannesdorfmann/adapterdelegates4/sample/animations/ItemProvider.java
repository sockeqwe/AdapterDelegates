package com.hannesdorfmann.adapterdelegates4.sample.animations;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DiffUtil;

/**
 * @author Hannes Dorfmann
 */

public class ItemProvider {

    public enum Modification {
        COLOR_CHANGED, TEXT_CHANGED
    }

    private List<Item> currentList;
    private int itemId = 0;

    private Random rand = new Random();

    ItemProvider() {
        currentList = new ArrayList<>(20);

        for (int i = 0; i < 20; i++) {
            currentList.add(newItem());
        }
    }

    private Item newItem() {
        itemId++;
        return new Item(itemId, "Item " + itemId, color());
    }

    private int color() {

        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);

        return Color.argb(255, r, g, b);
    }

    public List<Item> getCurrentList() {
        return currentList;
    }

    public Pair<List<Item>, DiffUtil.DiffResult> add() {
        List<Item> newlist = copyCurrent();

        newlist.add(2, newItem());
        newlist.add(4, newItem());

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ItemDiff(currentList, newlist));

        currentList = newlist;
        return Pair.create(newlist, diffResult);
    }

    /**
     * @return null if removement cant be executed because min list size is required
     */
    @Nullable
    public Pair<List<Item>, DiffUtil.DiffResult> remove() {
        if (currentList.size() <= 6) {
            return null;
        }
        List<Item> newlist = copyCurrent();

        newlist.remove(1);
        newlist.remove(3);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ItemDiff(currentList, newlist));

        currentList = newlist;
        return Pair.create(newlist, diffResult);
    }

    public Pair<List<Item>, DiffUtil.DiffResult> modify() {
        List<Item> newlist = copyCurrent();

        Item c1 = newlist.get(0).copy();
        c1.color = color();
        newlist.set(0, c1);

        Item c2 = newlist.get(5).copy();
        c2.text = c2.text + " - Updated";
        newlist.set(5, c2);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ItemDiff(currentList, newlist));

        currentList = newlist;
        return Pair.create(newlist, diffResult);
    }

    public Pair<List<Item>, DiffUtil.DiffResult> move() {
        List<Item> newlist = copyCurrent();

        Item c2 = newlist.remove(5);
        c2.color = color();
        newlist.add(1, c2);

        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(new ItemDiff(currentList, newlist), true);

        currentList = newlist;
        return Pair.create(newlist, diffResult);
    }

    private List<Item> copyCurrent() {
        List<Item> copy = new ArrayList<>(currentList);
        return copy;
    }

    private static class ItemDiff extends DiffUtil.Callback {

        private List<Item> oldList;
        private List<Item> newList;

        private ItemDiff(List<Item> oldList, List<Item> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).id == newList.get(newItemPosition).id;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Item oldItem = oldList.get(oldItemPosition);
            Item newItem = newList.get(newItemPosition);
            return oldItem.text.equals(newItem.text) && oldItem.color == newItem.color;
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {

            Item oldItem = oldList.get(oldItemPosition);
            Item newItem = newList.get(newItemPosition);

            if (!oldItem.text.equals(newItem.text)) {
                return Modification.TEXT_CHANGED;
            } else {
                return Modification.COLOR_CHANGED;
            }
        }
    }
}
