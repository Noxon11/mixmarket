package com.example.mixmarket

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class ProductDetail : AppCompatActivity() {

    private lateinit var cartManager: CartManager

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        cartManager = CartManager.getInstance(this)

        val product = intent.getParcelableExtra("product",Product::class.java)
        if (product != null) {
            displayProductDetails(product)
        }
    }

    private fun displayProductDetails(product: Product) {
        val imageViewDetail: ImageView = findViewById(R.id.imageViewDetail)
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val priceTextView: TextView = findViewById(R.id.priceTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val ratingTextView: TextView = findViewById(R.id.ratingTextView)
        val countTextView: TextView = findViewById(R.id.countTextView)
        val addToCartButton: Button = findViewById(R.id.addToCartButton)

        Picasso.get().load(product.image).into(imageViewDetail)
        titleTextView.text = product.title
        descriptionTextView.text = product.description
        priceTextView.text = String.format("%.2f €", product.price)
        ratingTextView.text = getStarRatingString(product.rating.rate)
        countTextView.text = String.format("(%.0f)", product.rating.count)

        addToCartButton.setOnClickListener {
            addToCart(product)
        }
    }

    private fun getStarRatingString(rating: Double): String {

        val numStars = rating.toInt()


        val fullStars = "★".repeat(numStars)
        val halfStar = if (rating-numStars >= 0.5) "★" else "☆"
        val emptyStar = "☆".repeat(4-numStars)

        return "$fullStars$halfStar$emptyStar"
    }

    private fun addToCart(product: Product) {
        lifecycleScope.launch {
            try {
                cartManager.addToCart(product)
                Toast.makeText(this@ProductDetail, "Produit ajouté au panier", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@ProductDetail, "Erreur lors de l'ajout au panier", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
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


