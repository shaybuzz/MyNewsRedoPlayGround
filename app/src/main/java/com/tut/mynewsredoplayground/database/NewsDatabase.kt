package com.tut.mynewsredoplayground.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tut.mynewsredoplayground.model.Article

@Database(entities = arrayOf(Article::class), version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getNewsDao(): NewsDao

    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getInstance(context: Context): NewsDatabase {
            val tmp = INSTANCE
            if (tmp != null) return tmp
            synchronized(this) {
                val tmpInstance = INSTANCE
                if (tmpInstance == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        NewsDatabase::class.java,
                        "news_db"
                    ).build()

                }
            }
            return INSTANCE!!
        }
    }
}