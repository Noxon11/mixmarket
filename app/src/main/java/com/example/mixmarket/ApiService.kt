package com.example.mixmarket

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("products/categories")
    suspend fun getCategories(): List<String>

    @GET("products/{id}")
    suspend fun getRandomProduct(@Path("id") id: Int): Product
}