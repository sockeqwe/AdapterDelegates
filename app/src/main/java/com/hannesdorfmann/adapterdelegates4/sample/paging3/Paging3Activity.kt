package com.hannesdorfmann.adapterdelegates4.sample.paging3

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterItemProvider
import com.hannesdorfmann.adapterdelegates4.paging3.PagingDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterLayoutContainer
import com.hannesdorfmann.adapterdelegates4.dsl.adapterViewBinding
import com.hannesdorfmann.adapterdelegates4.sample.R
import com.hannesdorfmann.adapterdelegates4.sample.databinding.ItemDogBinding
import com.hannesdorfmann.adapterdelegates4.sample.model.Cat
import com.hannesdorfmann.adapterdelegates4.sample.model.DisplayableItem
import com.hannesdorfmann.adapterdelegates4.sample.model.Dog
import com.hannesdorfmann.adapterdelegates4.sample.model.Snake
import kotlinx.android.synthetic.main.item_snake.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class Paging3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagination)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = PagingDelegationAdapter<DisplayableItem>(
            callback,
            CatAdapterDelegate(this),
            adapterViewBinding<Dog, DisplayableItem, ItemDogBinding>(
                viewBinding = { layoutInflater, parent ->
                    ItemDogBinding.inflate(layoutInflater, parent, false)
                }
            ) {
                bind { binding.name.text = item.name }
            },
            adapterLayoutContainer<Snake, DisplayableItem>(R.layout.item_snake) {
                bind {
                    name.text = item.name
                    race.text = item.race
                }
            }
        )

        recyclerView.adapter = adapter

        lifecycleScope.launch {
            Pager<Int, DisplayableItem>(
                PagingConfig(pageSize = 10, enablePlaceholders = false)
            ) {
                SampleDatasource()
            }.flow
                .cachedIn(lifecycleScope)
                .collectLatest { adapter.submitData(it) }
        }
    }

    private val callback = object : DiffUtil.ItemCallback<DisplayableItem>() {
        override fun areItemsTheSame(oldItem: DisplayableItem, newItem: DisplayableItem): Boolean {
            if (oldItem === newItem || oldItem == newItem) {
                return true
            }
            return oldItem.javaClass == newItem.javaClass
        }

        override fun areContentsTheSame(oldItem: DisplayableItem, newItem: DisplayableItem): Boolean {
            // TODO: implement that properly
            return false
        }

    }
}

private class CatAdapterDelegate(
    context: Context
) : AdapterDelegate<AdapterItemProvider<DisplayableItem>>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun isForViewType(items: AdapterItemProvider<DisplayableItem>, position: Int): Boolean {
        return items.getAdapterItem(position) is Cat
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return CatViewHolder(layoutInflater.inflate(R.layout.item_cat, parent, false))
    }

    override fun onBindViewHolder(items: AdapterItemProvider<DisplayableItem>, position: Int, holder: RecyclerView.ViewHolder, payloads: MutableList<Any>) {
        val vh = holder as CatViewHolder
        val cat = items.getAdapterItem(position) as Cat

        vh.name.text = cat.name
    }

    class CatViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.name)
    }
}