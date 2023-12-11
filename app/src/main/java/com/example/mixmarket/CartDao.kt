package com.example.mixmarket

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartDao {

    @Insert
    suspend fun insertCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteCartItem(productId: Long)

    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItems(): List<CartItem>

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    fun getCartItem(productId: Long): CartItem?

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

}
