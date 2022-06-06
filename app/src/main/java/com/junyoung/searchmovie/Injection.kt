package com.junyoung.searchmovie

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.junyoung.searchmovie.data.local.MovieDatabase
import com.junyoung.searchmovie.data.paging.MoviePagingRepository
import com.junyoung.searchmovie.data.remote.api.MovieService
import com.junyoung.searchmovie.viewmodel.MovieSearchViewModelFactory

object Injection {

    private fun providePagingRepository(context: Context): MoviePagingRepository {
        return MoviePagingRepository(MovieService.create(), MovieDatabase.getDatabase(context))
    }

    fun provideMovieSearchViewModelFactory(context: Context, owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return MovieSearchViewModelFactory(owner, providePagingRepository(context))
    }
}