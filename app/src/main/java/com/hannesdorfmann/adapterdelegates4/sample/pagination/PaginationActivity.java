package com.hannesdorfmann.adapterdelegates4.sample.pagination;

import android.os.Bundle;
import android.util.Log;

import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager;
import com.hannesdorfmann.adapterdelegates4.paging.PagedListDelegationAdapter;
import com.hannesdorfmann.adapterdelegates4.sample.R;
import com.hannesdorfmann.adapterdelegates4.sample.adapterdelegates.AdvertisementAdapterDelegate;
import com.hannesdorfmann.adapterdelegates4.sample.adapterdelegates.CatAdapterDelegate;
import com.hannesdorfmann.adapterdelegates4.sample.adapterdelegates.DogAdapterDelegate;
import com.hannesdorfmann.adapterdelegates4.sample.adapterdelegates.GeckoAdapterDelegate;
import com.hannesdorfmann.adapterdelegates4.sample.adapterdelegates.SnakeListItemAdapterDelegate;
import com.hannesdorfmann.adapterdelegates4.sample.model.DisplayableItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PaginationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagination);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        AdapterDelegatesManager<List<DisplayableItem>> delegatesManager =
                new AdapterDelegatesManager<List<DisplayableItem>>()
                        .addDelegate(new AdvertisementAdapterDelegate(this))
                        .addDelegate(new CatAdapterDelegate(this))
                        .addDelegate(new DogAdapterDelegate(this))
                        .addDelegate(new GeckoAdapterDelegate(this))
                        .addDelegate(new SnakeListItemAdapterDelegate(this))
                        .setFallbackDelegate(new LoadingAdapterDelegate(this));


        final PagedListDelegationAdapter<DisplayableItem> adapter =
                new PagedListDelegationAdapter<DisplayableItem>(delegatesManager, callback);

        recyclerView.setAdapter(adapter);


        LiveData<PagedList<DisplayableItem>> pagedListLiveData =
                new LivePagedListBuilder<>(new SampleDataSource.Factory(), 20)
                        .setBoundaryCallback(new PagedList.BoundaryCallback<DisplayableItem>() {
                            @Override
                            public void onZeroItemsLoaded() {
                                Log.d("PaginationSource", "onZeroItemsLoaded");
                                super.onZeroItemsLoaded();
                            }

                            @Override
                            public void onItemAtFrontLoaded(@NonNull DisplayableItem itemAtFront) {
                                Log.d("PaginationSource", "onItemAtFrontLoaded "+itemAtFront);
                                super.onItemAtFrontLoaded(itemAtFront);
                            }

                            @Override
                            public void onItemAtEndLoaded(@NonNull DisplayableItem itemAtEnd) {
                                Log.d("PaginationSource", "onItemAtEndLoaded "+itemAtEnd);
                                super.onItemAtEndLoaded(itemAtEnd);
                            }
                        })
                        .build();

        pagedListLiveData.observe(this, new Observer<PagedList<DisplayableItem>>() {
            @Override
            public void onChanged(PagedList<DisplayableItem> displayableItems) {
                adapter.submitList(displayableItems);
            }
        });

    }


    private DiffUtil.ItemCallback<DisplayableItem> callback = new DiffUtil.ItemCallback<DisplayableItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull DisplayableItem oldItem, @NonNull DisplayableItem newItem) {
            if (oldItem == newItem || oldItem.equals(newItem))
                return true;

            return oldItem.getClass().equals(newItem.getClass());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DisplayableItem oldItem, @NonNull DisplayableItem newItem) {
            // TODO implement that properly
            return false;
        }
    };
}
