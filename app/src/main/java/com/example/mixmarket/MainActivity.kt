package com.example.mixmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private lateinit var categoryContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        categoryContainer = findViewById(R.id.categoryContainer)
        fetchCategories()
        fetchRandomProduct()

        val buttonAllProduct = findViewById<Button>(R.id.buttonAllProduct)
        buttonAllProduct.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                        val intent = Intent(this@MainActivity, ListProduct::class.java)
                        startActivity(intent)

            }
        }
    }

    private fun fetchCategories() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val categories = ApiClient.categoryService.getCategories()
                createCategoryButtons(categories)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createCategoryButtons(categories: List<String>) {
        for (category in categories) {
            val button = Button(this)
            button.text = category
            button.setOnClickListener {
                val intent = Intent(this@MainActivity, ListProduct::class.java)
                intent.putExtra("categoryName", category)
                startActivity(intent)
            }

            categoryContainer.addView(button)
        }
    }

    fun onBasketClick(view: View) {
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }


    private fun fetchRandomProduct() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val randomProductId = (1..ApiClient.productService.getProducts().size).random()
                val randomProduct = ApiClient.randomProductService.getRandomProduct(randomProductId)
                displayRandomProduct(randomProduct)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun displayRandomProduct(randomProduct: Product) {
        val imageView: ImageView = findViewById(R.id.imageView)
        val randomProductName: TextView = findViewById(R.id.RandomProductName)
        val randomProductPrice: TextView = findViewById(R.id.RandomProductPrice)
        val randomProductDesc: TextView = findViewById(R.id.RandomeProductDesc)

        Picasso.get().load(randomProduct.image).into(imageView)
        randomProductName.text = randomProduct.title
        randomProductPrice.text = "Seulement ${randomProduct.price}€ au lieu de ${(randomProduct.price*1.25).roundToInt()}€"
        randomProductDesc.text = randomProduct.description
    }
}