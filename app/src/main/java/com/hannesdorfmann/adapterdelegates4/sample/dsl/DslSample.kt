package com.hannesdorfmann.adapterdelegates4.sample.dsl

import android.util.Log
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.hannesdorfmann.adapterdelegates4.sample.R
import com.hannesdorfmann.adapterdelegates4.sample.model.Cat
import com.hannesdorfmann.adapterdelegates4.sample.model.DisplayableItem
import kotlinx.android.synthetic.main.item_cat.*

// Example
fun catDelegate() = adapterDelegate<Cat, DisplayableItem>(R.layout.item_cat) {

    val name = findViewById<TextView>(R.id.name)
    name.setOnClickListener {
        Log.d("Click", "Click on $item")
    }

    bind {
        name.text = item.name
    }
}

fun fooDelegate() = adapterDelegateLayoutContainer<Cat, DisplayableItem>(R.layout.item_cat) {

    name.setOnClickListener {
        Log.d("Click", "Click on layoutcontainer $item")
    }

    bind {
        name.text = item.name
    }
}
