package com.example.gozlebrowser.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gozlebrowser.R
import com.example.gozlebrowser.presentation.searchPage.SearchFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        val fragment = SearchFragment()
        // if your webview can go back it will go back
        if (fragment.binding.webView.canGoBack())
            fragment.binding.webView.goBack()
        // if your webview cannot go back
        // it will exit the application
        else
            super.onBackPressed()
    }
}