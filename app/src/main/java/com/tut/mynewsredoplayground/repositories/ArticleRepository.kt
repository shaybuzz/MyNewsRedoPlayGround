package com.tut.mynewsredoplayground.repositories

import androidx.lifecycle.LiveData
import com.tut.mynewsredoplayground.model.Article

interface ArticleRepository {

    val articles: LiveData<List<Article>>
    val articlesSearch: LiveData<List<Article>>
    val savedArticles: LiveData<List<Article>>

    suspend fun fetch(country: String, page: Int = 1)
    suspend fun search(subject: String, page: Int = 1)
    suspend fun saveArticle(article: Article)
    suspend fun unSaveArticle(article: Article)
    suspend fun deleteAll()
}