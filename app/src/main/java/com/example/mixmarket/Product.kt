package com.example.mixmarket


data class Product(
        val id: String,
        val title: String,
        val image: String,
        val price: Double,
        val description: String,
        val category: String,
        val rating: Rating
    )
data class Rating(
    val rate: Double,
    val count: Double
)
