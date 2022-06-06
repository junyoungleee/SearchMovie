package com.junyoung.searchmovie.data.paging

import android.text.Html
import android.text.TextUtils
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.junyoung.searchmovie.data.local.MovieDatabase
import com.junyoung.searchmovie.data.model.Movie
import com.junyoung.searchmovie.data.model.RemoteKeys
import com.junyoung.searchmovie.data.paging.PagingConstant.DISPLAY_SIZE
import com.junyoung.searchmovie.data.remote.api.MovieService
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val query: String,
    private val service: MovieService,
    private val database: MovieDatabase
) : RemoteMediator<Int, Movie>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(DISPLAY_SIZE)  ?: STARTING_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKes = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKes?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKes != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        try {
            val apiResponse = service.searchMovie(query, state.config.pageSize, page)
            val responses = apiResponse.items
            val movies = responses.map {
                val movieCode = it.link.split("code=")[1].toLong()
                val movieTitle = Html.fromHtml(it.title, Html.FROM_HTML_MODE_LEGACY).toString()
                val imgUrl = if(it.image == "") null else it.image
                Movie(movieId = movieCode, title = movieTitle, link = it.link, image= imgUrl, date = it.date, rating= it.rating)
            }
            val endOfPaginationReached = apiResponse.total == apiResponse.start + apiResponse.display - 1

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeyDao().clearRemoteKeys()
                    database.movieDao().clearMovies()
                }
                val prevKey = if (page == STARTING_INDEX) null else page - DISPLAY_SIZE
                val nextKey = if (endOfPaginationReached) null else page + DISPLAY_SIZE
                val keys = movies.map {
                    RemoteKeys(movieId = it.movieId, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeyDao().insertAll(keys)
                database.movieDao().insertAll(movies)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { movie ->
            database.remoteKeyDao().remoteKeysMovieId(movie.movieId)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { movie ->
                database.remoteKeyDao().remoteKeysMovieId(movie.movieId)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.movieId?.let { movieId ->
                database.remoteKeyDao().remoteKeysMovieId(movieId)
            }
        }
    }

}