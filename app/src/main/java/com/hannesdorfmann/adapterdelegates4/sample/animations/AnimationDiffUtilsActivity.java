package com.hannesdorfmann.adapterdelegates4.sample.animations;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager;
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter;
import com.hannesdorfmann.adapterdelegates4.sample.R;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AnimationDiffUtilsActivity extends AppCompatActivity {

    private ListDelegationAdapter<List<Item>> adapter;
    private ItemProvider itemProvider = new ItemProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_diff_utils);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItems();
            }
        });

        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItems();
            }
        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItems();
            }
        });

        findViewById(R.id.move).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveItems();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        ItemAdapterDelegate itemAdapterDelegate = new ItemAdapterDelegate(getLayoutInflater());

        AdapterDelegatesManager<List<Item>> adapterDelegatesManager = new AdapterDelegatesManager<>();
        adapterDelegatesManager.addDelegate(itemAdapterDelegate);

        adapter = new ListDelegationAdapter<List<Item>>(adapterDelegatesManager);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setItems(itemProvider.getCurrentList());
        adapter.notifyDataSetChanged();
    }

    private void addItems() {
        Pair<List<Item>, DiffUtil.DiffResult> add = itemProvider.add();
        adapter.setItems(add.first);
        add.second.dispatchUpdatesTo(adapter);
    }

    private void removeItems() {
        Pair<List<Item>, DiffUtil.DiffResult> remove = itemProvider.remove();
        if (remove == null) {
            Toast.makeText(this,
                    "Can't remove items because min list size is required. Please add some items before removing",
                    Toast.LENGTH_LONG).show();
        } else {
            adapter.setItems(remove.first);
            remove.second.dispatchUpdatesTo(adapter);
        }
    }

    private void updateItems() {
        Pair<List<Item>, DiffUtil.DiffResult> modify = itemProvider.modify();

        adapter.setItems(modify.first);
        modify.second.dispatchUpdatesTo(adapter);
    }

    private void moveItems() {
        Pair<List<Item>, DiffUtil.DiffResult> move = itemProvider.move();

        adapter.setItems(move.first);
        move.second.dispatchUpdatesTo(adapter);
    }

}
