package com.hannesdorfmann.adapterdelegates4.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Intent
import com.hannesdorfmann.adapterdelegates4.sample.animations.AnimationDiffUtilsActivity
import com.hannesdorfmann.adapterdelegates4.sample.databinding.ActivityMainBinding
import com.hannesdorfmann.adapterdelegates4.sample.model.*
import com.hannesdorfmann.adapterdelegates4.sample.pagination.PaginationActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val animals: List<DisplayableItem>
        get() {
            return mutableListOf<DisplayableItem>().apply {
                add(Cat("American Curl"))
                add(Cat("Baliness"))
                add(Cat("Bengal"))
                add(Cat("Corat"))
                add(Cat("Manx"))
                add(Cat("Nebelung"))
                add(Dog("Aidi"))
                add(Dog("Chinook"))
                add(Dog("Appenzeller"))
                add(Dog("Collie"))
                add(Snake("Mub Adder", "Adder"))
                add(Snake("Texas Blind Snake", "Blind snake"))
                add(Snake("Tree Boa", "Boa"))
                add(Gecko("Fat-tailed", "Hemitheconyx"))
                add(Gecko("Stenodactylus", "Dune Gecko"))
                add(Gecko("Leopard Gecko", "Eublepharis"))
                add(Gecko("Madagascar Gecko", "Phelsuma"))
                add(Advertisement())
                add(Advertisement())
                add(Advertisement())
                add(Advertisement())
                add(Advertisement())
                shuffle()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.adapter =
                MainListAdapter(this@MainActivity).apply {
                    items = animals
                }
            reptielsActivity.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        ReptilesActivity::class.java
                    )
                )
            }
            animations.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        AnimationDiffUtilsActivity::class.java
                    )
                )
            }
            diffActivity.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        DiffActivity::class.java
                    )
                )
            }
            pagination.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        PaginationActivity::class.java
                    )
                )
            }
        }
    }
}