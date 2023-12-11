package com.example.mixmarket

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.util.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartManager private constructor(context: Context) {

    private val cartDao: CartDao

    init {

        Log.e("MON LOG", "CartManager init")
        val database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "cart-database"
        ).build()

        cartDao = database.cartDao()
    }

    suspend fun addToCart(product: Product) {
        withContext(Dispatchers.IO) {
            val existingCartItem = cartDao.getCartItem(product.id)

            if (existingCartItem != null) {
                // Product is already in the cart, increment quantity
                val updatedCartItem = existingCartItem.copy(quantity = existingCartItem.quantity + 1)
                cartDao.updateCartItem(updatedCartItem)
            } else {
                // Product is not in the cart, add as a new item
                val cartItem = CartItem(
                    productId = product.id,
                    productName = product.title,
                    productPrice = product.price,
                    productImg = product.image,
                    quantity = 1
                )
                cartDao.insertCartItem(cartItem)
            }
        }
    }

    suspend fun updateCartItem(cartItem: CartItem) {
        withContext(Dispatchers.IO) {
            val cartItemFromDb = cartDao.getCartItem(cartItem.productId)
            cartItemFromDb?.let {
                val updatedCartItem = cartItemFromDb.copy(quantity = cartItem.quantity)
                cartDao.updateCartItem(updatedCartItem)
                println(" to ${cartItem.quantity}")
            }
        }
    }


    suspend fun deleteCartItem(cartItem: CartItem) {
        withContext(Dispatchers.IO) {
            cartDao.deleteCartItem(cartItem.id)
        }
    }

    suspend fun getCart(): List<CartItem> {
        return withContext(Dispatchers.IO) {
            val cartItems = cartDao.getAllCartItems()
            cartItems.map {
                CartItem(it.productId, it.productId, it.productName, it.productPrice,it.productImg,it.quantity)
            }
        }
    }
    suspend fun getAllCartItems(): List<CartItem>{
        return cartDao.getAllCartItems()
    }

    companion object {
        private var INSTANCE: CartManager? = null

        fun getInstance(context: Context): CartManager {
            if (INSTANCE == null) {
                synchronized(CartManager::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = CartManager(context)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
