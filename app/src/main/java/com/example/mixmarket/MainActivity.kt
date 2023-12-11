package com.example.mixmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var categoryContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        categoryContainer = findViewById(R.id.categoryContainer)
        fetchCategories()

        val buttonAllProduct = findViewById<Button>(R.id.buttonAllProduct)
        buttonAllProduct.setOnClickListener {
            // Récupérer la liste de catégories depuis l'API
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
                // Gérer les erreurs d'appel API pour les catégories
                e.printStackTrace()
            }
        }
    }

    private fun createCategoryButtons(categories: List<String>) {
        for (category in categories) {
            val button = Button(this)
            button.text = category
            button.setOnClickListener {
                // Filtrer les produits en fonction du nom de la catégorie
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
}
