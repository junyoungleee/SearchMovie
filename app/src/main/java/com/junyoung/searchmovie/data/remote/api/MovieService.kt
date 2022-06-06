package com.junyoung.searchmovie.data.remote.api

import com.junyoung.searchmovie.data.remote.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("search/movie.json")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("display") display: Int,
        @Query("start") start: Int
    ): MovieSearchResponse

    companion object {
        private const val BASE_URL = "https://openapi.naver.com/v1/"

        fun create(): MovieService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(AuthInterceptor())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieService::class.java)
        }
    }

    private class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val request = request().newBuilder()
                .addHeader("X-Naver-Client-Id", BuildConfig.CLIENT_ID)
                .addHeader("X-Naver-Client-Secret", BuildConfig.CLIENT_SECRET)
                .build()
            return chain.proceed(request)
        }
    }
}