package com.tut.mynewsredoplayground.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tut.mynewsredoplayground.model.Article

@Dao
interface NewsDao {

    @Query("SELECT * from articles")
    fun getArticles(): LiveData<List<Article>>

    @Query("SELECT * from articles WHERE id=:articleId")
    fun getArticle(articleId: Int): LiveData<Article>

    @Insert
    suspend fun upsert(articles: List<Article>): List<Long>

    @Insert
    suspend fun upsert(article: Article): Long

    @Query("DELETE from articles")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(article: Article)
}