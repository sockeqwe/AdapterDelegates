package com.hannesdorfmann.adapterdelegates3;

/**
 * Interface that should be implemented by data items for being used
 * in {@link DiffDelegationAdapter}. Basically it declares methods for
 * resolving items by some unique ID and by content. It makes using of
 * {@link android.support.v7.util.DiffUtil.ItemCallback} super easy.
 *
 * {@link DiffItem#getItemHash()} is supposed to be implemented through calculation of {@link Object#hashCode()}
 * This approach allows us to resolve if content of items has been changed or not.
 * But feel free to make your own implementation
 *
 * {@link DiffItem#getItemId()} is pretty clear and you can use IDs of your items. Obviously it has to be unique.
 * But sometimes when you have some adapter items that don't have any ID (for example footers,
 * headers or section dividers) I suggest you to use item layout ID. It will look like this:
 *
 * {@code
 *    class MyFooterItem implements DiffItem {
 *      @Override
 *      long getItemId() {
 *          return (long)R.layout.item_footer
 *      }
 *
 *      @Override
 *      int getItemHash() {
 *          return hashCode()
 *      }
 *    }
 * }
 *
 * @author Sergey Opivalov
 */
public interface DiffItem {

    long getItemId();

    int getItemHash();
}
