package com.junyoung.searchmovie.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.junyoung.searchmovie.Injection
import com.junyoung.searchmovie.MovieApplication
import com.junyoung.searchmovie.R
import com.junyoung.searchmovie.databinding.ActivityMainBinding
import com.junyoung.searchmovie.view.adapter.MovieAdapter
import com.junyoung.searchmovie.view.adapter.MovieLoadStateAdapter
import com.junyoung.searchmovie.viewmodel.MovieSearchViewModel
import com.junyoung.searchmovie.viewmodel.MovieSearchViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieVM: MovieSearchViewModel

    private lateinit var movieAdapter : MovieAdapter
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        movieVM = ViewModelProvider(
            this, Injection.provideMovieSearchViewModelFactory(this, this)
        ).get(MovieSearchViewModel::class.java)

        binding.view = this
        initRecyclerView()
    }

    private fun initRecyclerView() {
        movieAdapter = MovieAdapter(MovieAdapter.MovieClickListener { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        })

        binding.rcMovieList.adapter = movieAdapter.withLoadStateHeaderAndFooter(
            header = MovieLoadStateAdapter { movieAdapter.retry() },
            footer = MovieLoadStateAdapter { movieAdapter.retry() }
        )

        movieAdapter.addLoadStateListener { loadState ->
            with(binding) {
                rcMovieList.isVisible = loadState.mediator?.refresh is LoadState.NotLoading
                pbLoading.isVisible = loadState.mediator?.refresh is LoadState.Loading
                btnRetry.isVisible = loadState.mediator?.refresh is LoadState.Error
            }
        }
    }

    fun searchMovie() {
        val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            movieVM.searchMovie("${binding.etSearchWord.text}").collect {
                movieAdapter.submitData(it)
            }
        }
    }

    fun goToRecentSearch() {
        val intent = Intent(this, RecentSearchActivity::class.java)
        startActivity(intent)
    }
}