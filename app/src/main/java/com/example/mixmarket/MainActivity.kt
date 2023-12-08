package com.example.mixmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonAllProduct = findViewById<Button>(R.id.buttonAllProduct)
        buttonAllProduct.setOnClickListener {
            startActivity(Intent(this, ListProduct::class.java))
        }
    }
}