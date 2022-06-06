package com.junyoung.searchmovie.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.junyoung.searchmovie.data.model.Movie

@Dao
interface MovieDAO {

    // Movie 객체 리스트 삽입(중복 시, 객체를 대체함)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    @Query("SELECT * FROM movie ORDER BY id")
    fun getMovie(): PagingSource<Int, Movie>

    // 모든 데이터 삭제
    @Query("DELETE FROM movie")
    suspend fun clearMovies()
}