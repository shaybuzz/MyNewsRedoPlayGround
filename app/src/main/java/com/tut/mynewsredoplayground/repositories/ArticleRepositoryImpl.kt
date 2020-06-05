package com.tut.mynewsredoplayground.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tut.mynewsredoplayground.database.NewsDao
import com.tut.mynewsredoplayground.model.Article
import com.tut.mynewsredoplayground.network.NewsApi
import com.tut.mynewsredoplayground.utils.Resource
import kotlinx.coroutines.delay
import timber.log.Timber
import java.lang.Exception

class ArticleRepositoryImpl(private val newsApi: NewsApi, private val newsDao: NewsDao) :
    ArticleRepository {

    override val savedArticles: LiveData<List<Article>> = newsDao.getArticles()

    private val _fetchResponse = MutableLiveData<Resource<List<Article>>>()
    override val fetchResponse: LiveData<Resource<List<Article>>> = _fetchResponse


    override suspend fun fetch(country: String, page: Int) {
        try {
            _fetchResponse.postValue(Resource.Loading())
            val newsResponse = newsApi.getArticles(country, page)
            if (newsResponse.status.equals("ok")) {
                //newsDao.upsert(newsResponse.articles)
                //mimic delay
                //delay(2000)
                _fetchResponse.postValue(Resource.Success(newsResponse.articles))
            } else {
                _fetchResponse.postValue(Resource.Failure(message = "error ${newsResponse.status}"))
            }
        } catch (exception: Exception) {
            Timber.d("#### on error ${exception.message}")
            _fetchResponse.postValue(Resource.Failure(exception, "error in fetch data"))
        }

    }

    override suspend fun deleteAll() {
        newsDao.deleteAll()
    }
}