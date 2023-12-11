package com.example.mixmarket

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartAdapter(private val cartItemListener: CartItemListener) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var cartItems: List<CartItem> = listOf()

    fun setCartItems(items: List<CartItem>) {
        // Aggregate items by ID and calculate total quantity
        val aggregatedList = items.groupBy { it.productId }
            .map { (productId, items) ->
                val totalQuantity = items.sumOf { it.quantity }
                items.first().copy(quantity = totalQuantity)
            }

        cartItems = aggregatedList
        notifyDataSetChanged()
        println("test")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }


    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        holder.bind(item, cartItemListener)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(cartItem: CartItem, cartItemListener: CartItemListener) {
            val itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
            val itemPriceTextView: TextView = itemView.findViewById(R.id.itemPriceTextView)
            val itemImageView: ImageView = itemView.findViewById(R.id.itemImageView)
            val itemQuantityTextView: TextView = itemView.findViewById(R.id.itemQuantityTextView)


            Picasso.get().load(cartItem.productImg).into(itemImageView)
            itemNameTextView.text = cartItem.productName
            itemPriceTextView.text = String.format("%.2f €", cartItem.productPrice)
            itemQuantityTextView.text = "Quantity: ${cartItem.quantity}"
            println("Item already in cart. Incrementing quantity to ${cartItem.quantity}")

            val addImg: ImageView = itemView.findViewById(R.id.addImg)
            val removeImg: ImageView = itemView.findViewById(R.id.removeImg)
            val deleteImg: ImageView = itemView.findViewById(R.id.deleteImg)

            addImg.setOnClickListener { cartItemListener.onAddClick(cartItem) }
            removeImg.setOnClickListener { cartItemListener.onRemoveClick(cartItem) }
            deleteImg.setOnClickListener { cartItemListener.onDeleteClick(cartItem) }
        }
    }

    interface CartItemListener {
        fun onAddClick(cartItem: CartItem)
        fun onRemoveClick(cartItem: CartItem)
        fun onDeleteClick(cartItem: CartItem)
    }

}

