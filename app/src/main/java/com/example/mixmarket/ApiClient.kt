package com.example.mixmarket

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
        private const val BASE_URL = "https://fakestoreapi.com/"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val productService: ProductService = retrofit.create(ProductService::class.java)
    }
