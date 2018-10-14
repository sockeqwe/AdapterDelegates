package com.hannesdorfmann.adapterdelegates4.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hannesdorfmann.adapterdelegates4.sample.animations.AnimationDiffUtilsActivity;
import com.hannesdorfmann.adapterdelegates4.sample.model.Advertisement;
import com.hannesdorfmann.adapterdelegates4.sample.model.Cat;
import com.hannesdorfmann.adapterdelegates4.sample.model.DisplayableItem;
import com.hannesdorfmann.adapterdelegates4.sample.model.Dog;
import com.hannesdorfmann.adapterdelegates4.sample.model.Gecko;
import com.hannesdorfmann.adapterdelegates4.sample.model.Snake;
import com.hannesdorfmann.adapterdelegates4.sample.pagination.PaginationActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        MainAdapter adapter = new MainAdapter(this, getAnimals());
        rv.setAdapter(adapter);


        findViewById(R.id.reptielsActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReptilesActivity.class));
            }
        });

        findViewById(R.id.animations).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnimationDiffUtilsActivity.class));
            }
        });

        findViewById(R.id.diffActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DiffActivity.class));
            }
        });

        findViewById(R.id.pagination).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PaginationActivity.class));
            }
        });
    }

    private List<DisplayableItem> getAnimals() {
        List<DisplayableItem> animals = new ArrayList<>();

        animals.add(new Cat("American Curl"));
        animals.add(new Cat("Baliness"));
        animals.add(new Cat("Bengal"));
        animals.add(new Cat("Corat"));
        animals.add(new Cat("Manx"));
        animals.add(new Cat("Nebelung"));
        animals.add(new Dog("Aidi"));
        animals.add(new Dog("Chinook"));
        animals.add(new Dog("Appenzeller"));
        animals.add(new Dog("Collie"));
        animals.add(new Snake("Mub Adder", "Adder"));
        animals.add(new Snake("Texas Blind Snake", "Blind snake"));
        animals.add(new Snake("Tree Boa", "Boa"));
        animals.add(new Gecko("Fat-tailed", "Hemitheconyx"));
        animals.add(new Gecko("Stenodactylus", "Dune Gecko"));
        animals.add(new Gecko("Leopard Gecko", "Eublepharis"));
        animals.add(new Gecko("Madagascar Gecko", "Phelsuma"));
        animals.add(new Advertisement());
        animals.add(new Advertisement());
        animals.add(new Advertisement());
        animals.add(new Advertisement());
        animals.add(new Advertisement());

        Collections.shuffle(animals);
        return animals;
    }
}
