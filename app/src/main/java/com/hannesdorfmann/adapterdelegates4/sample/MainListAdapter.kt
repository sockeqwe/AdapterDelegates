package com.hannesdorfmann.adapterdelegates4.sample

import android.app.Activity
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.sample.adapterdelegates.AdvertisementAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.sample.adapterdelegates.DogAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.sample.adapterdelegates.GeckoAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.sample.adapterdelegates.SnakeListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.sample.dsl.catAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.sample.model.DisplayableItem

class MainListAdapter(activity: Activity) : ListDelegationAdapter<List<DisplayableItem>>(
    AdvertisementAdapterDelegate(activity),
    catAdapterDelegate(),
    DogAdapterDelegate(activity),
    GeckoAdapterDelegate(activity),
    SnakeListItemAdapterDelegate(activity)
)