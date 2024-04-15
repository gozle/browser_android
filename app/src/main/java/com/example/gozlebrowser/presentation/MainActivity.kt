package com.example.gozlebrowser.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gozlebrowser.R
import com.example.gozlebrowser.presentation.fragment.SearchFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun launchSearchFragment() {
        val searchFragment = SearchFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                searchFragment
            )
            .addToBackStack(null)
            .commit()
    }
}