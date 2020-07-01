package com.hannesdorfmann.adapterdelegates4.sample.paging3

import androidx.paging.PagingSource
import com.hannesdorfmann.adapterdelegates4.sample.model.Cat
import com.hannesdorfmann.adapterdelegates4.sample.model.DisplayableItem
import com.hannesdorfmann.adapterdelegates4.sample.model.Dog
import com.hannesdorfmann.adapterdelegates4.sample.model.Snake
import java.lang.IllegalStateException
import java.util.*

class SampleDatasource : PagingSource<Int, DisplayableItem>() {

    private val random = Random()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DisplayableItem> {
        val result = mutableListOf<DisplayableItem>()
        for (i in 0 until params.loadSize) {
            when (val itemType = random.nextInt(3)) {
                0 -> result.add(Cat("Cat $i"))
                1 -> result.add(Dog("Dog $i"))
                2 -> result.add(Snake("Snake $i", "Race"))
                else -> throw IllegalStateException("Random returned $itemType")
            }
        }

        return LoadResult.Page(data = result, prevKey = params.key, nextKey = (params.key ?: 0) + 1)
    }
}