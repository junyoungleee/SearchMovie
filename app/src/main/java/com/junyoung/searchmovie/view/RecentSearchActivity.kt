package com.junyoung.searchmovie.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.junyoung.searchmovie.R
import com.junyoung.searchmovie.databinding.ActivityMainBinding
import com.junyoung.searchmovie.databinding.ActivityRecentSearchBinding

class RecentSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recent_search)

        binding.activity = this
    }
}