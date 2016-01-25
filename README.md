# AdapterDelegates
Read the motivation for this project in [my blog post](http://hannesdorfmann.com/android/adapter-delegates/).

## Dependencies
This library is available on maven central:

```groovy
compile 'com.hannesdorfmann:adapterdelegates:1.1.0'
```

[![Build Status](https://travis-ci.org/sockeqwe/AdapterDelegates.svg?branch=master)](https://travis-ci.org/sockeqwe/AdapterDelegates)

## Idea
The idea of this library is to build your adapters by composing reusable components.

> Favor composition over inheritance

The idea is that you define an `AdapterDelegate` for each view type. This delegate is responsible for creating ViewHolder and binding ViewHolder for a certain viewtype.
An `AdapterDelegate` get added to an `AdapterDelegatesManager`. This manager is the man in the middle between `RecyclerView.Adapter` and each `AdapterDelegate`.

For example:
```java
public class CatAdapterDelegate extends AbsAdapterDelegate<List<Animal>> {

  private LayoutInflater inflater;

  public CatAdapterDelegate(Activity activity, int viewType) {
    super(viewType);
    inflater = activity.getLayoutInflater();
  }

  @Override public boolean isForViewType(@NonNull List<Animal> items, int position) {
    return items.get(position) instanceof Cat;
  }

  @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
    return new CatViewHolder(inflater.inflate(R.layout.item_cat, parent, false));
  }

  @Override public void onBindViewHolder(@NonNull List<Animal> items, int position,
      @NonNull RecyclerView.ViewHolder holder) {

    CatViewHolder vh = (CatViewHolder) holder;
    Cat cat = (Cat) items.get(position);

    vh.name.setText(cat.getName());
  }

  static class CatViewHolder extends RecyclerView.ViewHolder {

    public TextView name;

    public CatViewHolder(View itemView) {
      super(itemView);
      name = (TextView) itemView.findViewById(R.id.name);
    }
  }
}
```

Then an `AnimalAdapter` could look like this:

```java
public class AnimalAdapter extends RecyclerView.Adapter {

  private AdapterDelegatesManager<List<Animal>> delegatesManager;
  private List<Animal> items;

  public AnimalAdapter(Activity activity, List<Animal> items) {
    this.items = items;

    // Delegates
    delegatesManager = new AdapterDelegatesManager<>();
    delegatesManager.addDelegate(new CatAdapterDelegate(activity, 0));
    delegatesManager.addDelegate(new DogAdapterDelegate(activity, 1));
    delegatesManager.addDelegate(new GeckoAdapterDelegate(activity, 2));
    delegatesManager.addDelegate(new SnakeAdapterDelegate(activity, 3));

  }

  @Override public int getItemViewType(int position) {
    return delegatesManager.getItemViewType(items, position);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return delegatesManager.onCreateViewHolder(parent, viewType);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    delegatesManager.onBindViewHolder(items, position, holder);
  }

  @Override public int getItemCount() {
    return items.size();
  }
}
```

## Reducing boilerplate code
As you have seen in the code snipped above this may require to write the same boiler plate code  again and again to hook in `AdapterDelegatesManager` to `Adapter`.
This can be reduced by extending either from `ListDelegationAdapter` if the data source the adapter displays is `java.util.List<?>` or `AbsDelegationAdapter` which is a more general one (not limited to `java.util.List`)

In example the same `AnimalAdapter` from above could be simplified as follows by exending from `ListDelegationAdapter`:

```java
public class AnimalAdapter extends ListDelegationAdapter<List<Animal>> {

  public AnimalAdapter(Activity activity, List<Animal> items) {

    // DelegatesManager is a protected Field in ListDelegationAdapter
    delegatesManager.addDelegate(new CatAdapterDelegate(activity, 0));
    delegatesManager.addDelegate(new DogAdapterDelegate(activity, 1));
    delegatesManager.addDelegate(new GeckoAdapterDelegate(activity, 2));
    delegatesManager.addDelegate(new SnakeAdapterDelegate(activity, 3));

    // Set the items from super class.
    setItems(items);
  }
}
```

## Fallback AdapterDelegate
What if your adapter's data source contains a certain element you don't have registered an `AdapterDelegate` for? In this case the `AdapterDelegateManager` will throw an exception at runtime. However, this is not always what you want. You can specify a fallback `AdapterDelegate` that will be used if no other `AdapterDelegate` has been found to handle a certain `AdapterDelegate`.

```java
AdapterDelegate fallbackDelegate = ...;
adapterDelegateManager.setFallbackDelegate( fallbackDelegate );
```

Please note that the fallback delegate must return an integer value from `fallbackDelegate.getItemViewType()` that **doesn't** conflict with any other `AdapterDelegate` added by `adapterDelegateManager.addDelegate( fooDelegate )`.
The manager will check for conflicts at runtime. To minimize the risk of conflicts you can use `AbsFallbackAdapterDelegate` as base class for your fallback implementation. `AbsFallbackAdapterDelegate` uses `Integer.MAX_VALUE - 1` internally to avoid conflicts with other adapter delegates. However, you are free to write your own adapter delegate fallback that doesn't extend from `AbsFallbackAdapterDelegate`. Note also that boolean return type of `isForViewType()` of a fallback delegate will be ignored (will not be take into account).


## License
```
Copyright 2015 Hannes Dorfmann

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
