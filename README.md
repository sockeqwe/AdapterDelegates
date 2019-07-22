# AdapterDelegates
Read the motivation for this project in [my blog post](http://hannesdorfmann.com/android/adapter-delegates).

**[If you use Kotlin then this library has a convenient DSL you can use. Check out that section in the documentation below](https://github.com/sockeqwe/AdapterDelegates#kotlin-dsl)**.

## Dependencies
This library is available on maven central:

```groovy
implementation 'com.hannesdorfmann:adapterdelegates4:4.1.1'
```
[![Build Status](https://travis-ci.org/sockeqwe/AdapterDelegates.svg?branch=master)](https://travis-ci.org/sockeqwe/AdapterDelegates)

Please note that since 4.0 the group id has been changed to `adapterdelegates4`.

### Snapshot

```groovy
implementation 'com.hannesdorfmann:adapterdelegates4:4.1.2-SNAPSHOT'
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

Please note that `onBindViewHolder()` last parameter `payloads` is null unless you use from `adapter.notify`.  There are more methods like `onViewRecycled(ViewHolder)`, `onFailedToRecycleView(ViewHolder)`,
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
As you have seen in the code snippet above this may require to write the same boiler plate code again and again to hook in `AdapterDelegatesManager` to `Adapter`.
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

## DiffUtil & ListAdapter = AsyncListDifferDelegationAdapter
Support library 27.0.1 introduced `ListAdapter` - the new extension of `RecyclerView.Adapter` that uses `AsyncListDiffer` internally. It does calculating diff in the background thread by default and does all particular animations for you items collection. Hence you don't need carry about `notify*` methods, `AsyncListDiffer` does all the job for you. And AdapterDelegates supports it too. 

This library offers the equivalent to `ListAdapter` which is called `AsyncListDifferDelegationAdapter` that can be used together with any regular `AdapterDelegate`.

```java
public class DiffAdapter extends AsyncListDifferDelegationAdapter<Animal> {
    public DiffAdapter() {
        super(DIFF_CALLBACK) // Your diff callback for diff utils
        delegatesManager
            .addDelegate(new DogAdapterDelegate());
            .addDelegate(new CatAdapterDelegate());
    }
}
```

## Kotlin DSL
There are 2 more artifacts for kotlin users that allow you to write Adapter Delegates more convenient by providing a `DSL`:

```
implementation 'com.hannesdorfmann:adapterdelegates4-kotlin-dsl:4.1.1'

// If you use Kotlin Android Extensions and synthetic properties (alternative to findViewById())
implementation 'com.hannesdorfmann:adapterdelegates4-kotlin-dsl-layoutcontainer:4.1.1'
```

Now instead of creating your own class which extends `AdapterDelegate<T>` and implement the `onCreateViewHolder` and `onBindViewHolder` you can use the following Kotlin DSL to write the same `CatListItemAdapterDelegate` shown in the example above:


```kotlin
fun catAdapterDelegate(itemClickedListener : (Cat) -> Unit) = adapterDelegate<Cat, Animal>(R.layout.item_cat) {

    // This is the initializer block where you initialize the ViewHolder.
    // Its called one time only in onCreateViewHolder.
    // this is where you can call findViewById() and setup click listeners etc.

    val name : TextView = findViewById(R.id.name)
    name.setClickListener { itemClickedListener(item) } // Item is automatically set for you. It's set lazily though (set in onBindViewHolder()). So only use it for deferred calls like clickListeners.

    bind { diffPayloads -> // diffPayloads is a List<Any> containing the Payload from your DiffUtils
        // This is called anytime onBindViewHolder() is called
        name.text = item.name // Item is of type Cat and is the current bound item.
    }
}
```

In case you want to use kotlin android extensions and synthetic properties (as alternative to findViewById()) use `adapterDelegateLayoutContainer` instead of `adapterDelegate` like this:

```kotlin
fun catAdapterDelegate(itemClickedListener : (Cat) -> Unit) = adapterDelegateLayoutContainer<Cat, Animal>(R.layout.item_cat) {

    name.setClickListener { itemClickedListener(item) } // no need for findViewById(). Name is imported as synthetic property from kotlinx.android.synthetic.main.item_cat

    bind { diffPayloads ->
        name.text = item.name
    }
}
```

As you see, thanks to Kotlin DSL you can write the same adapter in much less code.
`isForViewType()` is implemented by checking the two generic parameters.
In the example above it is `Cat instanceof Animal`.
If you want to provide your own `isForViewType()` implementation you have to provide a parameter `on` and return true or false:

```kotlin
adapterDelegate<Cat, Animal> (
    layout = R.layout.item_cat,
    on = { item: Animal, items: List, position: Int ->
        if (item is Cat && position == 0)
            true // return true: this adapterDelegate handles it
        else
            false // return false
    }
){
    ...
    bind { ... }
}
```

The same `on` parameter is available for `adapterDelegateLayoutContainer()` DSL.


### Danger: Memory leaks!
Never ever use a top level `val` to hold a reference as top level `val` are static and will hold a reference to the adapter delegate and underlying ViewHolder and underlying android context (like activity) forever.
**Don't do this:**


```kotlin
// top level property inside CatDelegate.kt
val catDelegate = adapterDelegate<Cat, Animal> {
    ...
    bind { ... }
}
```

**Instead use top level functions:**

```kotlin
// top level function inside CatDelegate.kt
fun catAdapterDelegate() = adapterDelegate<Cat, Animal> {
   ...
   bind { ... }
}
```

## Pagination
There is an additional artifact for the pagination library:

```gradle
implementation 'com.hannesdorfmann:adapterdelegates4-pagination:4.1.1'
```

Use `PagedListDelegationAdapter`.

## Fallback AdapterDelegate
What if your adapter's data source contains a certain element you don't have registered an `AdapterDelegate` for? In this case the `AdapterDelegateManager` will throw an exception at runtime. However, this is not always what you want. You can specify a fallback `AdapterDelegate` that will be used if no other `AdapterDelegate` has been found to handle a certain view type.

```java
AdapterDelegate fallbackDelegate = ...;
adapterDelegateManager.setFallbackDelegate( fallbackDelegate );
```
Note also that boolean return type of `isForViewType()` of a fallback delegate will be ignored (will not be take into account). So it doesn't matter if you return true or false. You can use `AbsFallbackAdapterDelegate` that already implements `isForViewType()` so that you only have to override `onCreateViewHolder()` and `onBindViewHolder()` for your fallback adapter delegate.

## Version 3.x to 4.0 migration
`AdapterDelegates3` uses `com.android.support:recyclerview-v7:x.y.z` whereas `AdapterDelegates4` uses
`androidx.recyclerview:recyclerview:1.0.0`.
Migration should be easy. Just use IntelliJ IDE or Android Studio 'Replace in Path' (can be found inside `Edit` main menu then `Find` submenu):
Replace `com.hannesdorfmann.adapterdelegates3` with `com.hannesdorfmann.adapterdelegates4`.
You might also have to replace `android.support.v7.widget.RecyclerView` with `androidx.recyclerview.widget.RecyclerView` and
`android.support.annotation.NonNull` with `androidx.annotation.NonNull`.

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
