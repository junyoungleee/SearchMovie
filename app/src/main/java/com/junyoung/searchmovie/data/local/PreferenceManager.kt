package com.junyoung.searchmovie.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class PreferenceManager(context: Context) {

    private val TAG = "PREFERENCE_LOG"
    private val PREF_NAME = "movie_info"
    private val PREF_MODE = Context.MODE_PRIVATE
    var prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, PREF_MODE)

    private val gson = GsonBuilder().create()
    private val wordType: Type = object: TypeToken<List<String>>() {}.type
    private var wordList = mutableListOf<String>()

    private val SEARCH_WORDS = "search_words"
    private var searchWords: String?
        get() = prefs.getString(SEARCH_WORDS, "")
        set(value) {
            prefs.edit().putString(SEARCH_WORDS, value).apply()
        }

    fun getSearchWord(): List<String> {
        if (searchWords == "") {
            wordList = mutableListOf()
        } else {
            wordList.clear()
            wordList.addAll(gson.fromJson(searchWords, wordType))
        }

        Log.d(TAG, "$wordList")
        return wordList
    }

    fun updateSearchWord(word: String) {
        if (searchWords != "") {
            wordList.clear()
            wordList.addAll(gson.fromJson(searchWords, wordType))
            if (wordList.indexOf(word) != -1) {
                wordList.remove(word)
            } else if (wordList.size == 10) {
                wordList.removeLast()
            }
        }
        wordList.add(0, word)

        Log.d(TAG, "$wordList")
        val listStr = gson.toJson(wordList, wordType)
        searchWords = listStr
    }

}