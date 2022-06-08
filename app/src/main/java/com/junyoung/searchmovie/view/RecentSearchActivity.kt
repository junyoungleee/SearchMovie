package com.junyoung.searchmovie.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.junyoung.searchmovie.MovieApplication
import com.junyoung.searchmovie.R
import com.junyoung.searchmovie.databinding.ActivityMainBinding
import com.junyoung.searchmovie.databinding.ActivityRecentSearchBinding
import com.junyoung.searchmovie.view.adapter.MovieAdapter
import com.junyoung.searchmovie.view.adapter.MovieLoadStateAdapter
import com.junyoung.searchmovie.view.adapter.RecentSearchAdapter

class RecentSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecentSearchBinding
    private lateinit var wordAdapter : RecentSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recent_search)

        binding.view = this
        initRecyclerView()
    }

    private fun initRecyclerView() {
        wordAdapter = RecentSearchAdapter(RecentSearchAdapter.WordClickListener { word ->
            MovieApplication.pref.updateSearchWord(word)

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("search_word", word)
            setResult(RESULT_OK, intent)
            finish()
        })

        val divider = DividerItemDecoration(this, LinearLayoutManager(this).orientation)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.item_divider)!!)

        with(binding.rcRecentSearchMovie) {
            adapter = wordAdapter
            addItemDecoration(divider)
        }
        wordAdapter.submitList(MovieApplication.pref.getSearchWord())
    }
}