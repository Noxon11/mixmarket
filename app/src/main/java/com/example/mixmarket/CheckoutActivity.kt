package com.example.mixmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CheckoutActivity : AppCompatActivity() {
        lateinit var myWebView: WebView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_checkout)

            myWebView = findViewById(R.id.webView)

            myWebView.settings.javaScriptEnabled = true

            myWebView.loadUrl("https://www.paypal.me/Arthurl19")

            val Home = findViewById<Button>(R.id.Home)
            Home.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    val intent = Intent(this@CheckoutActivity, MainActivity::class.java)
                    startActivity(intent)

                }
            }
        }


        override fun onBackPressed() {
            if (myWebView.canGoBack()) {
                myWebView.goBack()
            } else {
                super.onBackPressed()
            }
        }

    fun onHomeClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onBasketClick(view: View) {
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }

    }