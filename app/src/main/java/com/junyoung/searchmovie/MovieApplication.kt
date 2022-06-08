package com.junyoung.searchmovie

import android.app.Application
import com.junyoung.searchmovie.data.local.PreferenceManager

class MovieApplication : Application() {
    companion object {
        lateinit var pref: PreferenceManager
    }

    override fun onCreate() {
        super.onCreate()
        pref = PreferenceManager(applicationContext)
    }
}