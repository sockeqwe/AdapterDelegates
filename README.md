# AdapterDelegates
Read the motivation for this project in [my blog post](http://hannesdorfmann.com/android/adapter-delegates).

## Dependencies
This library is available on maven central:

```groovy
compile 'com.hannesdorfmann:adapterdelegates3:3.0.0'
```
[![Build Status](https://travis-ci.org/sockeqwe/AdapterDelegates.svg?branch=master)](https://travis-ci.org/sockeqwe/AdapterDelegates)

Please note that since 3.0 the group id has been changed to `adapterdelegates3`.

### Snapshot

```groovy
compile 'com.hannesdorfmann:adapterdelegates3:3.0.1-SNAPSHOT'
```

You also have to add the url to the snapshot repository:

```groovy
allprojects {
  repositories {
    ...

    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
}
```

### Changelog
See [releases section](https://github.com/sockeqwe/AdapterDelegates/releases)

## Idea
The idea of this library is to build your adapters by composing reusable components.

> Favor composition over inheritance

The idea is that you define an `AdapterDelegate` for each view type. This delegate is responsible for creating ViewHolder and binding ViewHolder for a certain viewtype.
An `AdapterDelegate` get added to an `AdapterDelegatesManager`. This manager is the man in the middle between `RecyclerView.Adapter` and each `AdapterDelegate`.

For example:
```java
public class CatAdapterDelegate extends AdapterDelegate<List<Animal>> {

  private LayoutInflater inflater;

  public CatAdapterDelegate(Activity activity) {
    inflater = activity.getLayoutInflater();
  }

  @Override public boolean isForViewType(@NonNull List<Animal> items, int position) {
    return items.get(position) instanceof Cat;
  }

  @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
    return new CatViewHolder(inflater.inflate(R.layout.item_cat, parent, false));
  }

  @Override public void onBindViewHolder(@NonNull List<Animal> items, int position,
      @NonNull RecyclerView.ViewHolder holder, @Nullable List<Object> payloads) {

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

Please note that `onBindViewHolder()` last parameter `payloads` is null unless you use from `adpater.notify`.  There are more methods like `onViewRecycled(ViewHolder)`, `onFailedToRecycleView(ViewHolder)`,
`onViewAttachedToWindow(ViewHolder)` and `onViewDetachedFromWindow(ViewHolder)` you can override in your `AdapterDelegate` class.


Then an `AnimalAdapter` could look like this:

```java
public class AnimalAdapter extends RecyclerView.Adapter {

  private AdapterDelegatesManager<List<Animal>> delegatesManager;
  private List<Animal> items;

  public AnimalAdapter(Activity activity, List<Animal> items) {
    this.items = items;

    delegatesManager = new AdapterDelegatesManager<>();

    // AdapterDelegatesManager internally assigns view types integers
    delegatesManager.addDelegate(new CatAdapterDelegate(activity))
                    .addDelegate(new DogAdapterDelegate(activity))
                    .addDelegate(new GeckoAdapterDelegate(activity));

    // You can explicitly assign integer view type if you want to
    delegatesManager.addDelegate(23, new SnakeAdapterDelegate(activity));
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
As you have seen in the code snipped above this may require to write the same boiler plate code again and again to hook in `AdapterDelegatesManager` to `Adapter`.
This can be reduced by extending either from `ListDelegationAdapter` if the data source the adapter displays is `java.util.List<?>` or `AbsDelegationAdapter` which is a more general one (not limited to `java.util.List`)

#### ListDelegationAdapter
For example the same `AnimalAdapter` from above could be simplified as follows by extending from `ListDelegationAdapter`:

```java
public class AnimalAdapter extends ListDelegationAdapter<List<Animal>> {

  public AnimalAdapter(Activity activity, List<Animal> items) {

    // DelegatesManager is a protected Field in ListDelegationAdapter
    delegatesManager.addDelegate(new CatAdapterDelegate(activity))
                    .addDelegate(new DogAdapterDelegate(activity))
                    .addDelegate(new GeckoAdapterDelegate(activity))
                    .addDelegate(23, new SnakeAdapterDelegate(activity));

    // Set the items from super class.
    setItems(items);
  }
}
```
#### AbsListItemAdapterDelegate
Also you may have noticed that you often have to write boilerplate code to cast items and ViewHolders when working with list of items as adapters dataset source.
`AbsListItemAdapterDelegate` can help you here. Let's take this class to create a `CatListItemAdapterDelegate` similar to the `CatAdapterDelegate` from top of this page but without the code for casting items.

```java
public class CatListItemAdapterDelegate extends AbsListItemAdapterDelegate<Cat, Animal, CatViewHolder> {

  private LayoutInflater inflater;

  public CatAdapterDelegate(Activity activity) {
    inflater = activity.getLayoutInflater();
  }

  @Override public boolean isForViewType(Animal item, List<Animal> items, int position) {
    return item instanceof Cat;
  }

  @Override public CatViewHolder onCreateViewHolder(ViewGroup parent) {
    return new CatViewHolder(inflater.inflate(R.layout.item_cat, parent, false));
  }

  @Override public void onBindViewHolder(Cat item, CatViewHolder vh, @Nullable List<Object> payloads) {
    vh.name.setText(item.getName());
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

As you see, instead of writing code that casts list item to Cat we can use `AbsListItemAdapterDelegate` to do the same job (by declaring generic types).

## Fallback AdapterDelegate
What if your adapter's data source contains a certain element you don't have registered an `AdapterDelegate` for? In this case the `AdapterDelegateManager` will throw an exception at runtime. However, this is not always what you want. You can specify a fallback `AdapterDelegate` that will be used if no other `AdapterDelegate` has been found to handle a certain view type.

```java
AdapterDelegate fallbackDelegate = ...;
adapterDelegateManager.setFallbackDelegate( fallbackDelegate );
```
Note also that boolean return type of `isForViewType()` of a fallback delegate will be ignored (will not be take into account). So it doesn't matter if you return true or false. You can use `AbsFallbackAdapterDelegate` that already implements `isForViewType()` so that you only have to override `onCreateViewHolder()` and `onBindViewHolder()` for your fallback adapter delegate.


## Migrating from `2.x` to `3.0`
In contrast to `2.x` in `3.0` `AdapterDelegate` is no longer a interface, but rather an abstract class.
To keep version backward compatible with project that are already using `1.x` or `2.x` the package has been renamed to `com.hannesdorfmann.adapterdelegates3` and also the artifact id has been renamed to `adapterdelegates3`.

See the [releases section](https://github.com/sockeqwe/AdapterDelegates/releases) for more information about changes.

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
