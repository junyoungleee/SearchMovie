package com.junyoung.searchmovie.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.junyoung.searchmovie.data.local.dao.MovieDAO
import com.junyoung.searchmovie.data.local.dao.RemoteKeyDAO
import com.junyoung.searchmovie.data.model.Movie
import com.junyoung.searchmovie.data.model.RemoteKeys

@Database(entities = [Movie::class, RemoteKeys::class], version = 4, exportSchema = false)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDAO
    abstract fun remoteKeyDao(): RemoteKeyDAO

    companion object {
        private var INSTANCE: MovieDatabase? = null
        fun getDatabase(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java, "movieDB")
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .addMigrations(MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE TempMoive('id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "'movieId' INTEGER NOT NULL, 'title' TEXT NOT NULL, 'link' TEXT NOT NULL, 'image' TEXT, " +
                        "'date' TEXT NOT NULL, 'rating' TEXT NOT NULL, 'query' TEXT NOT NULL)")
                database.execSQL("DROP TABLE Movie")
                database.execSQL("ALTER TABLE TempMoive RENAME TO Movie")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE TempMoive('id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "'title' TEXT NOT NULL, 'link' TEXT NOT NULL, 'image' TEXT, " +
                        "'date' TEXT NOT NULL, 'rating' TEXT NOT NULL)")
                database.execSQL("DROP TABLE Movie")
                database.execSQL("ALTER TABLE TempMoive RENAME TO Movie")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE TempMoive('id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "'movieId' INTEGER NOT NULL, 'title' TEXT NOT NULL, 'link' TEXT NOT NULL, 'image' TEXT, " +
                        "'date' TEXT NOT NULL, 'rating' TEXT NOT NULL)")
                database.execSQL("DROP TABLE Movie")
                database.execSQL("ALTER TABLE TempMoive RENAME TO Movie")
            }
        }

    }

}