package com.tut.mynewsredoplayground.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tut.mynewsredoplayground.database.NewsDao
import com.tut.mynewsredoplayground.model.Article
import com.tut.mynewsredoplayground.network.NewsApi
import com.tut.mynewsredoplayground.utils.Resource

class ArticleRepositoryImpl(private val newsApi: NewsApi, private val newsDao: NewsDao) :
    ArticleRepository {

    override val savedArticles: LiveData<List<Article>> = newsDao.getArticles()

    private val _fetchResponse = MutableLiveData<Resource<List<Article>>>()
    override val fetchResponse: LiveData<Resource<List<Article>>> = _fetchResponse

    private val _searchFetchResponse = MutableLiveData<Resource<List<Article>>>()
    override val searchFetchResponse: LiveData<Resource<List<Article>>> = _searchFetchResponse

    override suspend fun fetch(country: String, page: Int) {
        try {
            _fetchResponse.postValue(Resource.Loading())
            val newsResponse = newsApi.getArticles(country, page)
            if (newsResponse.status.equals("ok")) {
                //newsDao.upsert(newsResponse.articles)

                //delay(2000) //mimic network delay
                _fetchResponse.postValue(Resource.Success(newsResponse.articles))
            } else {
                _fetchResponse.postValue(Resource.Failure(message = "error ${newsResponse.status}"))
            }
        } catch (exception: Exception) {
            _fetchResponse.postValue(Resource.Failure(exception, "error in fetch data"))
        }
    }

    override suspend fun search(subject: String, page: Int) {
        try {
            _searchFetchResponse.postValue(Resource.Loading())
            val newsResponse = newsApi.search(subject, page)
            if (newsResponse.status.equals("ok")) {
                _searchFetchResponse.postValue(Resource.Success(newsResponse.articles))
            } else {
                _searchFetchResponse.postValue(Resource.Failure(message = "error ${newsResponse.status}"))
            }
        } catch (exception: Exception) {
            _searchFetchResponse.postValue(Resource.Failure(exception, "error in fetch data"))
        }
    }

    override suspend fun deleteAll() {
        newsDao.deleteAll()
    }
}