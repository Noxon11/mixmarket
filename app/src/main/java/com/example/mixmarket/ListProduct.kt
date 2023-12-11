package com.example.mixmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ListProduct : AppCompatActivity(), ProductClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryContainer: LinearLayout
    private lateinit var allProducts: List<Product>
    private lateinit var categoryName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_product)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        categoryContainer = findViewById(R.id.categoryContainer)
        categoryName = intent.getStringExtra("categoryName") ?: ""
        addAllButton()
        fetchCategories()
        fetchProducts()
    }

    private fun addAllButton() {
        val allButton = Button(this)
        allButton.text = "All"
        allButton.setOnClickListener {
            updateRecyclerView(allProducts)
        }
        categoryContainer.addView(allButton)
    }

    private fun fetchCategories() {
        lifecycleScope.launch {
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
                val filteredProducts = filterProductsByCategory(category)
                updateRecyclerView(filteredProducts)
            }
            categoryContainer.addView(button)
        }
    }

    private fun fetchProducts() {
        lifecycleScope.launch {
            try {
                allProducts = ApiClient.productService.getProducts()
                if (categoryName.isNotEmpty()) {
                    val filteredProducts = filterProductsByCategory(categoryName)
                    updateRecyclerView(filteredProducts)
                } else {
                    updateRecyclerView(allProducts)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun filterProductsByCategory(category: String): List<Product> {
        // Filtre les produits en fonction de la catégorie
        return allProducts.filter { it.category == category }
    }

    private fun updateRecyclerView(products: List<Product>) {
        val adapter = ProductAdapter(products, this)
        recyclerView.adapter = adapter
    }

    override fun onProductClick(product: Product) {
        // Ouvrir une activité de détails du produit lorsqu'un produit est cliqué
        val intent = Intent(this, ProductDetail::class.java)
        val bundle = Bundle().apply {
            putParcelable("product", product)
        }
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun onHomeClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onBasketClick(view: View) {
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }
}
