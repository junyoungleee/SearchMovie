package com.junyoung.searchmovie.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.savedstate.SavedStateRegistryOwner
import com.junyoung.searchmovie.data.model.Movie
import com.junyoung.searchmovie.data.paging.MoviePagingRepository
import kotlinx.coroutines.flow.Flow

class MovieSearchViewModel(
    private val repository: MoviePagingRepository
): ViewModel() {

    private var currentSearchWord: String? = null
    private var currentSearchResult: Flow<PagingData<Movie>>? = null

    fun searchMovie(query: String): Flow<PagingData<Movie>> {
        val lastResult = currentSearchResult
        if (query == currentSearchWord && lastResult != null) {
            return lastResult
        }
        currentSearchWord = query
        val newResult = repository.getMovieSearchResultStream(query)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}

class MovieSearchViewModelFactory(
    owner: SavedStateRegistryOwner, private val repository: MoviePagingRepository
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel?> create(
        key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        if (modelClass.isAssignableFrom(MovieSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieSearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}