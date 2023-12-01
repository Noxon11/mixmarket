package com.example.mixmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

    class AllProduct : AppCompatActivity() {
        private lateinit var recyclerView: RecyclerView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_all_product)

            recyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)

            fetchData()
        }

        private fun fetchData() {
            GlobalScope.launch(Dispatchers.Main) {
                val products = withContext(Dispatchers.IO) {
                    ApiClient.productService.getProducts()
                }

                val adapter = ProductAdapter(products)
                recyclerView.adapter = adapter
            }
        }
    }
