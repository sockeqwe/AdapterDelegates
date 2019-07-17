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

package com.hannesdorfmann.adapterdelegates4;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * An adapter implementation designed for items organized in a {@link List}. This adapter
 * implementation is ready to go. All you have to do is to add {@link AdapterDelegate}s to the
 * internal {@link AdapterDelegatesManager} i.e. in the constructor:
 *
 * <pre>
 * {@code
 *    class MyAdapter extends AbsDelegationAdapter<List<Foo>>{
 *        public MyAdapter(){
 *            this.delegatesManager.add(new FooAdapterDelegate());
 *            this.delegatesManager.add(new BarAdapterDelegate());
 *        }
 *    }
 * }
 * </pre>
 *
 * @param <T> The type of the items. Must be something that extends from List like List<Foo>
 * @author Hannes Dorfmann
 */
public class ListDelegationAdapter<T extends List<?>> extends AbsDelegationAdapter<T> {

    public ListDelegationAdapter() {
        super();
    }

    public ListDelegationAdapter(@NonNull AdapterDelegatesManager<T> delegatesManager) {
        super(delegatesManager);
    }

    /**
     * Adds a list of {@link AdapterDelegate}s
     *
     * @param delegates
     * @since 4.1.0
     */
    public ListDelegationAdapter(@NonNull AdapterDelegate<T>... delegates) {
        super(delegates);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
}
