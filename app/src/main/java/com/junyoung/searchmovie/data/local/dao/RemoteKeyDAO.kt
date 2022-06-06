package com.junyoung.searchmovie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.junyoung.searchmovie.data.model.RemoteKeys

@Dao
interface RemoteKeyDAO {

    // 네트워크에서 Movie를 가져올 때마다 Remotekey를 생성
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    // id를 기준으로 remoteKey 가져옴
    @Query("SELECT * FROM remote_keys WHERE movieId = :movieId")
    suspend fun remoteKeysMovieId(movieId: Long): RemoteKeys?

    // RemoteKeys 지우기 → 새 쿼리가 있을 때마다 사용
    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()

}