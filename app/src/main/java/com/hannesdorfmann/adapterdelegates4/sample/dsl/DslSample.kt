package com.hannesdorfmann.adapterdelegates4.sample.dsl

import android.util.Log
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.hannesdorfmann.adapterdelegates4.sample.R
import com.hannesdorfmann.adapterdelegates4.sample.databinding.ItemCatBinding
import com.hannesdorfmann.adapterdelegates4.sample.model.Cat
import com.hannesdorfmann.adapterdelegates4.sample.model.DisplayableItem
import kotlinx.android.synthetic.main.item_cat.name

// Example
fun catAdapterDelegate() = adapterDelegateLayoutContainer<Cat,
        DisplayableItem>(R.layout.item_cat) {

    name.setOnClickListener {
        Log.d("Click", "Click on $item")
    }

    bind {
        name.text = item.name
    }
}

fun cat2AdapterDelegate() = adapterDelegateViewBinding<Cat, DisplayableItem, ItemCatBinding>(
    { layoutInflater, root -> ItemCatBinding.inflate(layoutInflater, root, false) }
) {
    binding.name.setOnClickListener {
        Log.d("Click", "Click on $item")
    }
    bind {
        binding.name.text = item.name
    }
}
