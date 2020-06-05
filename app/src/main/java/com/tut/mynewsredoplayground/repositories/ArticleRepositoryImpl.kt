package com.tut.mynewsredoplayground.repositories

import androidx.lifecycle.LiveData
import com.tut.mynewsredoplayground.database.NewsDao
import com.tut.mynewsredoplayground.model.Article
import com.tut.mynewsredoplayground.network.NewsApi

class ArticleRepositoryImpl(private val newsApi: NewsApi, private val newsDao: NewsDao) : ArticleRepository {

    override val articles: LiveData<List<Article>> = newsDao.getArticles()

    override suspend fun fetch(page: Int) {
        val respond =newsApi.getArticles(page = page)
        newsDao.upsert(respond.articles)
    }
}