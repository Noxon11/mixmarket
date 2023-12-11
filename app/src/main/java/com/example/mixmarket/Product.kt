package com.example.mixmarket

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
        val id: Long,
        val title: String,
        val image: String,
        val price: Double,
        val description: String,
        val category: String,
        val rating: Rating
    ): Parcelable

@Parcelize
data class Rating(
    val rate: Double,
    val count: Double
): Parcelable
