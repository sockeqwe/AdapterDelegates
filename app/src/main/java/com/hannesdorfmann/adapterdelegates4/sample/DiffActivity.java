package com.hannesdorfmann.adapterdelegates4.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hannesdorfmann.adapterdelegates4.sample.model.DiffCat;
import com.hannesdorfmann.adapterdelegates4.sample.model.DiffDog;
import com.hannesdorfmann.adapterdelegates4.sample.model.DiffItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Sergey Opivalov
 */
public class DiffActivity extends AppCompatActivity {

    private AsyncListDifferAdapter adapter;

    private List<DiffItem> currentItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff);

        adapter = new AsyncListDifferAdapter();

        RecyclerView list = findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        adapter.setItems(getItems());

        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItems();
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItems();
            }
        });

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItems();
            }
        });

        findViewById(R.id.btn_shuffle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffleItems();
            }
        });
    }

    private List<DiffItem> getItems() {
        List<DiffItem> items = new ArrayList<DiffItem>() {{
            add(new DiffCat(0, "Barsik"));
            add(new DiffCat(1, "Kotik"));
            add(new DiffCat(2, "Persik"));
            add(new DiffCat(3, "Tomas"));
            add(new DiffDog(4, "Bobik", 14));
            add(new DiffDog(5, "Tuzik", 2));
            add(new DiffDog(6, "Sharik", 8));
            add(new DiffDog(7, "Arnold", 10));
            add(new DiffDog(8, "Buffy", 100));
        }};

        Collections.shuffle(items);
        currentItems = items;
        return currentItems;
    }

    private void updateItems() {
        List<DiffItem> items = new ArrayList<>(currentItems);
        try {
            DiffItem diffItem0 = items.get(0);
            if (diffItem0 instanceof DiffDog) {
                DiffDog dog = ((DiffDog) diffItem0);
                items.set(0, new DiffDog(dog.getId(), dog.getName() + "Updated", dog.getAge() + 5));
            } else if (diffItem0 instanceof DiffCat) {
                DiffCat cat = ((DiffCat) diffItem0);
                items.set(0, new DiffCat(cat.getId(), cat.getName() + "Updated"));
            }
            DiffItem diffItem3 = items.get(2);
            if (diffItem3 instanceof DiffDog) {
                DiffDog dog = ((DiffDog) diffItem3);
                items.set(2, new DiffDog(dog.getId(), dog.getName() + "Updated", dog.getAge() + 5));
            } else if (diffItem3 instanceof DiffCat) {
                DiffCat cat = ((DiffCat) diffItem3);
                items.set(2, new DiffCat(cat.getId(), cat.getName() + "Updated"));
            }
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(this, "Please add more items", Toast.LENGTH_SHORT).show();
        }
        currentItems = items;
        adapter.setItems(currentItems);
    }

    private void deleteItems() {
        List<DiffItem> items = new ArrayList<>(currentItems);
        try {
            items.remove(0);
            items.remove(3);
            items.remove(5);
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(this, "Please add more items", Toast.LENGTH_SHORT).show();
        }
        currentItems = items;
        adapter.setItems(currentItems);
    }

    private void addItems() {
        List<DiffItem> items = new ArrayList<>(currentItems);
        items.add(0, new DiffCat(items.size() + 1, "NewCat"));
        items.add(items.size(), new DiffDog(items.size() + 1, "AnotherNewDog", 17));
        currentItems = items;
        adapter.setItems(currentItems);
    }

    private void shuffleItems() {
        List<DiffItem> items = new ArrayList<>(currentItems);
        Collections.shuffle(items);
        currentItems = items;
        adapter.setItems(currentItems);
    }
}
