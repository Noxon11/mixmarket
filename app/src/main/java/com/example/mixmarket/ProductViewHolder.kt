package com.example.mixmarket

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)

    fun bind(product: Product) {

        Picasso.get().load(product.imageUrl).into(imageView)

        titleTextView.text = product.title
        priceTextView.text = "$${product.price}"
    }
}
