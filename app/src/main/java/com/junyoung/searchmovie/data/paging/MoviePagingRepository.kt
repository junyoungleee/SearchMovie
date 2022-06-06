package com.junyoung.searchmovie.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.junyoung.searchmovie.data.local.MovieDatabase
import com.junyoung.searchmovie.data.model.Movie
import com.junyoung.searchmovie.data.paging.PagingConstant.DISPLAY_SIZE
import com.junyoung.searchmovie.data.remote.api.MovieService
import kotlinx.coroutines.flow.Flow

class MoviePagingRepository(
    private val service: MovieService,
    private val database: MovieDatabase
) {

    fun getMovieSearchResultStream(query: String): Flow<PagingData<Movie>> {
        val pagingSourceFactory = {
            database.movieDao().getMovie()
        }

        @OptIn(ExperimentalPagingApi::class)
        val pager = Pager(
            config = PagingConfig(pageSize = DISPLAY_SIZE, enablePlaceholders = false),
            remoteMediator = MovieRemoteMediator(query, service, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow

        return pager
    }

}