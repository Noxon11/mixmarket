package com.example.mixmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity(), CartAdapter.CartItemListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartManager: CartManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.cartRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cartAdapter = CartAdapter(this)
        recyclerView.adapter = cartAdapter

        cartManager = CartManager.getInstance(application)

        displayCartItems()

        val checkoutButton: Button = findViewById(R.id.checkoutButton)
        checkoutButton.setOnClickListener {
            Toast.makeText(this, "Processus de paiement à implémenter", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayCartItems() {
        lifecycleScope.launch(Dispatchers.IO) {
            val cartItems = cartManager.getCart()
            withContext(Dispatchers.Main) {
                cartAdapter.setCartItems(cartItems)

            }
        }
    }

    fun onHomeClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onAddClick(cartItem: CartItem) {
        lifecycleScope.launch(Dispatchers.IO) {
            val updatedCartItem = cartItem.copy(quantity = cartItem.quantity + 1)
            cartManager.updateCartItem(updatedCartItem)
            //displayCartItems()

            val cartItems = cartManager.getCart()
            withContext(Dispatchers.Main) {
                cartAdapter.setCartItems(cartItems)
            }
        }
    }

    override fun onRemoveClick(cartItem: CartItem) {
        lifecycleScope.launch(Dispatchers.IO) {

            // sinon, supprimez l'article du panier
            if (cartItem.quantity > 1) {
                // Décrémentez la quantité
                val updatedCartItem = cartItem.copy(quantity = cartItem.quantity - 1)
                cartManager.updateCartItem(updatedCartItem)
            } else {
                // Supprimez l'article du panier
                cartManager.deleteCartItem(cartItem)
            }
            displayCartItems()
        }
    }

    override fun onDeleteClick(cartItem: CartItem) {
        lifecycleScope.launch(Dispatchers.IO) {
            cartManager.deleteCartItem(cartItem)
            displayCartItems()
        }
    }

}
