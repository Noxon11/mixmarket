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

        // Récupérer le nom de la catégorie de l'intent
        categoryName = intent.getStringExtra("categoryName") ?: ""

        // Ajouter le bouton "All" pour désélectionner le filtre
        addAllButton()

        // Récupérer les catégories et les produits depuis l'API
        fetchCategories()
        fetchProducts()
    }

    private fun addAllButton() {
        val allButton = Button(this)
        allButton.text = "All"
        allButton.setOnClickListener {
            // Afficher tous les produits sans filtre
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
                // Filtre les produits en fonction de la catégorie quand on clique dessus
                val filteredProducts = filterProductsByCategory(category)
                updateRecyclerView(filteredProducts)
            }
            categoryContainer.addView(button)
        }
    }

    private fun fetchProducts() {
        lifecycleScope.launch {
            try {
                // Récupérer tous les produits depuis l'API
                allProducts = ApiClient.productService.getProducts()

                // Si le nom de la catégorie n'est pas vide, appliquer le filtre
                if (categoryName.isNotEmpty()) {
                    val filteredProducts = filterProductsByCategory(categoryName)
                    updateRecyclerView(filteredProducts)
                } else {
                    // Sinon, afficher tous les produits
                    updateRecyclerView(allProducts)
                }

            } catch (e: Exception) {
                // Gérer les erreurs d'appel API pour les produits
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
