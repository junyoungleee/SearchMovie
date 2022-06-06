package com.junyoung.searchmovie.data.remote.api

import com.google.gson.annotations.SerializedName
import com.junyoung.searchmovie.data.model.Movie

data class MovieSearchResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("display") val display: Int,
    @SerializedName("items") val items: List<Movie> = emptyList(),
)
