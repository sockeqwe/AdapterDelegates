# AdapterDelegates
Read the motivation for this project in [my blog post](http://hannesdorfmann.com/android/adapter-delegates/).


## Idea
The idea of this library is to build your adapters by composing reusable components.

> Favor composition over inheritance

The idea is that you define an `AdapterDelegate` for each view type. This delegate is responsible for creating ViewHolder and binding ViewHolder for a certain viewtype.
An `AdapterDelegate` get added to an `AdapterDelegatesManager`. This manager is the man in the middle between `RecyclerView.Adapter` and each `AdapterDelegate`.

For example:
```java
public class CatAdapterDelegate extends AbsAdaterDelegate<List<Animal>> {

  private LayoutInflater inflater;

  public CatAdapterDelegate(Activity activity, int viewType) {
    super(viewType);
    inflater = activity.getLayoutInflater();
  }

  @Override public boolean isForViewType(@NonNull List<DisplayableItem> items, int position) {
    return items.get(position) instanceof Cat;
  }

  @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
    return new CatViewHolder(inflater.inflate(R.layout.item_cat, parent, false));
  }

  @Override public void onBindViewHolder(@NonNull List<DisplayableItem> items, int position,
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

  public MainAdapter(Activity activity, List<DisplayableItem> items) {
    this.items = items;

    // Delegates
    delegatesManager = new AdapterDelegatesManager<>();
    delegatesManager.addDelegate(new CatAdapterDelegate(activity, 1));
    delegatesManager.addDelegate(new DogAdapterDelegate(activity, 2));
    delegatesManager.addDelegate(new GeckoAdapterDelegate(activity, 3));
    delegatesManager.addDelegate(new SnakeAdapterDelegate(activity, 4));

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

## Reducing boiler plate code
As you have seen in the code snipped above this may require to write the same boiler plate code  again and again to hook in `AdapterDelegatesManager` to `Adapter`.
This can be reduced by extending either from `ListDelegationAdapter` if the datsource the adapter displays is `java.util.List<?>` or `AbsDelegationAdapter` which is a more general one (not limited to `java.util.List`)


## Dependencies
This library is available on maven central:

```groovy
compile `com.hannesdorfmann:adapterdelegates:1.0.0'
```