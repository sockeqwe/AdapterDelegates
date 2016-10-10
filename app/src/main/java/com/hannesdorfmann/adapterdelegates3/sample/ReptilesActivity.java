/*
 * Copyright (c) 2015 Hannes Dorfmann.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hannesdorfmann.adapterdelegates3.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.hannesdorfmann.adapterdelegates3.sample.model.DisplayableItem;
import com.hannesdorfmann.adapterdelegates3.sample.model.Gecko;
import com.hannesdorfmann.adapterdelegates3.sample.model.Snake;
import com.hannesdorfmann.adapterdelegates3.sample.model.UnknownReptile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.hannesdorfmann.adapterdelegates.sample.R;

public class ReptilesActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reptiels);

    RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
    rv.setLayoutManager(new LinearLayoutManager(this));
    ReptilesAdapter adapter = new ReptilesAdapter(this, getAnimals());
    rv.setAdapter(adapter);
  }

  private List<DisplayableItem> getAnimals() {
    List<DisplayableItem> animals = new ArrayList<>();

    animals.add(new Snake("Mub Adder", "Adder"));
    animals.add(new Snake("Texas Blind Snake", "Blind snake"));
    animals.add(new Snake("Tree Boa", "Boa"));
    animals.add(new Gecko("Fat-tailed", "Hemitheconyx"));
    animals.add(new Gecko("Stenodactylus", "Dune Gecko"));
    animals.add(new Gecko("Leopard Gecko", "Eublepharis"));
    animals.add(new Gecko("Madagascar Gecko", "Phelsuma"));
    animals.add(new UnknownReptile());

    animals.add(new Snake("Mub Adder", "Adder"));
    animals.add(new Snake("Texas Blind Snake", "Blind snake"));
    animals.add(new Snake("Tree Boa", "Boa"));
    animals.add(new Gecko("Fat-tailed", "Hemitheconyx"));
    animals.add(new Gecko("Stenodactylus", "Dune Gecko"));
    animals.add(new Gecko("Leopard Gecko", "Eublepharis"));
    animals.add(new Gecko("Madagascar Gecko", "Phelsuma"));

    animals.add(new Snake("Mub Adder", "Adder"));
    animals.add(new Snake("Texas Blind Snake", "Blind snake"));
    animals.add(new Snake("Tree Boa", "Boa"));
    animals.add(new Gecko("Fat-tailed", "Hemitheconyx"));
    animals.add(new Gecko("Stenodactylus", "Dune Gecko"));
    animals.add(new Gecko("Leopard Gecko", "Eublepharis"));
    animals.add(new Gecko("Madagascar Gecko", "Phelsuma"));

    Collections.shuffle(animals);
    return animals;
  }
}
