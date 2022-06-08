package com.junyoung.searchmovie.view

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieVM: MovieSearchViewModel

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
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

    override fun onStart() {
        super.onStart()
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val word = it.data?.getStringExtra("search_word").toString()
                searchMovie(word)
                binding.etSearchWord.setText(word)
            }
        }
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
                tvNoResult.isVisible = loadState.refresh is LoadState.NotLoading && movieAdapter.itemCount == 0
                rcMovieList.isVisible = loadState.mediator?.refresh is LoadState.NotLoading
                pbLoading.isVisible = loadState.mediator?.refresh is LoadState.Loading
                btnRetry.isVisible = loadState.mediator?.refresh is LoadState.Error
            }
        }
    }

    fun searchMovie(query: String) {
        if (query == "") {
            Toast.makeText(this, getString(R.string.toast_no_word), Toast.LENGTH_SHORT).show()
        } else {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                movieVM.searchMovie(query).collect {
                    movieAdapter.submitData(it)
                }
            }
            MovieApplication.pref.updateSearchWord(query)

            val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    fun goToRecentSearch() {
        val intent = Intent(this, RecentSearchActivity::class.java)
        activityResultLauncher.launch(intent)
    }
}