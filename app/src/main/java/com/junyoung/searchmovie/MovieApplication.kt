package com.junyoung.searchmovie

import android.app.Application
import com.junyoung.searchmovie.data.local.MovieDatabase
import com.junyoung.searchmovie.data.local.PreferenceManager
import com.junyoung.searchmovie.data.paging.MoviePagingRepository
import com.junyoung.searchmovie.data.remote.api.MovieService

class MovieApplication : Application() {
    companion object {
        lateinit var pref: PreferenceManager
    }

    override fun onCreate() {
        super.onCreate()
        pref = PreferenceManager(applicationContext)
    }
}