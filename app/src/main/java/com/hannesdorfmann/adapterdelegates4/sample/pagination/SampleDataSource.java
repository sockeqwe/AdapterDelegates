package com.hannesdorfmann.adapterdelegates4.sample.pagination;

import android.util.Log;

import com.hannesdorfmann.adapterdelegates4.sample.model.Advertisement;
import com.hannesdorfmann.adapterdelegates4.sample.model.Cat;
import com.hannesdorfmann.adapterdelegates4.sample.model.DisplayableItem;
import com.hannesdorfmann.adapterdelegates4.sample.model.Dog;
import com.hannesdorfmann.adapterdelegates4.sample.model.Gecko;
import com.hannesdorfmann.adapterdelegates4.sample.model.Snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;
import androidx.paging.PositionalDataSource;

public class SampleDataSource extends PositionalDataSource<DisplayableItem> {

    private Random random = new Random();

    private List<DisplayableItem> nextPage(int size) {
        List<DisplayableItem> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {

            int itemType = random.nextInt(5);
            switch (itemType) {
                case 0:
                    result.add(new Cat("Cat"));
                    break;

                case 1:
                    result.add(new Dog("Dog"));
                    break;

                case 2:
                    result.add(new Snake("Snake", "Viper"));
                    break;

                case 3:
                    result.add(new Gecko("Snake", "Viper"));
                    break;

                case 4:
                    result.add(new Advertisement());
                    break;


                default:
                    throw new IllegalStateException("Random returned " + itemType);
            }
        }

        return result;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<DisplayableItem> callback) {
        List<DisplayableItem> displayableItems = nextPage(params.requestedLoadSize);
        Log.d("PaginationSource", "pagesize " + params.pageSize + " placeholder " + params.placeholdersEnabled + " requested " + params.requestedLoadSize + " startpos " + params.requestedStartPosition);
        callback.onResult(displayableItems, 0, 200);
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<DisplayableItem> callback) {
        List<DisplayableItem> displayableItems = nextPage(params.loadSize);
        Log.d("PaginationSource", "loadSize " + params.loadSize + " startPosition " + params.startPosition);
        callback.onResult(displayableItems);
    }


    public static class Factory extends DataSource.Factory<Integer, DisplayableItem> {
        @Override
        public DataSource<Integer, DisplayableItem> create() {
            return new SampleDataSource();
        }
    }
}
