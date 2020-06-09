package com.tut.mynewsredoplayground.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tut.mynewsredoplayground.database.NewsDao
import com.tut.mynewsredoplayground.exceptions.ServerException
import com.tut.mynewsredoplayground.model.Article
import com.tut.mynewsredoplayground.model.NewsResponse
import com.tut.mynewsredoplayground.network.NewsApi
import com.tut.mynewsredoplayground.utils.Resource
import retrofit2.Response

class ArticleRepositoryImpl(private val newsApi: NewsApi, private val newsDao: NewsDao) :
    ArticleRepository {

    private val _articles = MutableLiveData<List<Article>>()
    override val articles: LiveData<List<Article>> = _articles

    private val _articlesSearch = MutableLiveData<List<Article>>()
    override val articlesSearch: LiveData<List<Article>> = _articlesSearch

    override val savedArticles: LiveData<List<Article>> = newsDao.getArticles()

    override suspend fun fetch(country: String, page: Int) {
        val newsResponse = newsApi.getArticles(country, page)
        if (newsResponse.status.equals("ok")) {
            _articles.postValue(newsResponse.articles)
        } else throw ServerException("result not valid ${newsResponse.status}")
    }

    override suspend fun search(subject: String, page: Int) {
        val newsResponse = newsApi.search(subject, page)
        if (newsResponse.status.equals("ok")) {
            _articlesSearch.postValue(newsResponse.articles)
        } else throw ServerException("result not valid ${newsResponse.status}")
    }

    override suspend fun saveArticle(article: Article) {
        newsDao.upsert(article)
    }

    override suspend fun unSaveArticle(article: Article) {
        newsDao.delete(article)
    }

    override suspend fun deleteAll() {
        newsDao.deleteAll()
    }
}