package com.hannesdorfmann.adapterdelegates4.sample.dsl

import android.util.Log
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.hannesdorfmann.adapterdelegates4.sample.R
import com.hannesdorfmann.adapterdelegates4.sample.model.Cat
import com.hannesdorfmann.adapterdelegates4.sample.model.DisplayableItem
import kotlinx.android.synthetic.main.item_cat.*

// Example
fun catAdapterDelegate() = adapterDelegateLayoutContainer<Cat, DisplayableItem>(R.layout.item_cat) {

    name.setOnClickListener {
        Log.d("Click", "Click on layoutcontainer $item")
    }

    bind {
        name.text = item.name
    }
}
