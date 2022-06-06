package com.junyoung.searchmovie.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val movieId: Long,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("link") val link: String,
    @field:SerializedName("image") val image: String?,
    @field:SerializedName("pubDate") val date: String,
    @field:SerializedName("userRating") val rating: String
)
