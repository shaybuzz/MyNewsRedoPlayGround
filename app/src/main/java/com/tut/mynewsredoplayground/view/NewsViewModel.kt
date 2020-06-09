package com.tut.mynewsredoplayground.view

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.net.NetworkInfo
import android.os.Build
import android.os.CountDownTimer
import androidx.lifecycle.*
import com.tut.mynewsredoplayground.database.NewsDatabase
import com.tut.mynewsredoplayground.exceptions.ServerException
import com.tut.mynewsredoplayground.model.Article
import com.tut.mynewsredoplayground.network.NetworkService
import com.tut.mynewsredoplayground.repositories.ArticleRepository
import com.tut.mynewsredoplayground.repositories.ArticleRepositoryImpl
import kotlinx.coroutines.launch
import timber.log.Timber

class NewsViewModel(application: Application) :
    AndroidViewModel(application) {

    private val newsApi = NetworkService.api
    private val newsDoa = NewsDatabase.getInstance(getApplication()).getNewsDao()
    private val articleRepository: ArticleRepository = ArticleRepositoryImpl(newsApi, newsDoa)

    val articles: LiveData<List<Article>> = articleRepository.articles
    val articlesSearch: LiveData<List<Article>> = articleRepository.articlesSearch
    val savedArticles = articleRepository.savedArticles

    var searchTerm = MutableLiveData<String>()

    //TODO better then observe forever??
    val isScrolling: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isScrollingTowardsEndOfList = MutableLiveData<Boolean>()

    private var fetchPage: Int = 1
    private var searchPage: Int = 1


    init {
        searchTerm.observeForever(Observer {
            countDownTimer.cancel()
            if (it.isNotEmpty()) {
                countDownTimer.start()
            }
        })
        fetchArticles("us")
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            articleRepository.saveArticle(article)
        }
    }

    fun unSaveArticle(article: Article) {
        viewModelScope.launch {
            articleRepository.unSaveArticle(article)
        }
    }

    fun searchArticles(subject: String) {
        viewModelScope.launch {
            try {
                if (hasInternetConnection()) {
                    articleRepository.search(subject, searchPage++)
                } else {
                    Timber.d("####check internet")
                }
            } catch (serverException: ServerException) {
                Timber.d("####got error ${serverException.errorMessage}")
            } catch (excption: Exception) {
                Timber.d("####got geenral error ${excption.message}")
            }
        }
    }

    fun fetchArticles(country: String) {
        viewModelScope.launch {
            try {
                if (hasInternetConnection()) {
                    articleRepository.fetch(country, fetchPage++)
                } else {
                    Timber.d("####check internet")
                }
            } catch (serverException: ServerException) {
                Timber.d("####got error ${serverException.errorMessage}")
            } catch (excption: Exception) {
                Timber.d("####got geenral error ${excption.message}")
            }

        }
    }

    private fun hasInternetConnection(): Boolean {
        val app = getApplication<NewsApp>()
        val cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            val activeNetwork = cm.activeNetwork ?: return false
            val capabilities = cm.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> return true
                else -> return false
            }
        }
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true

    }

    fun deleteAll() {
        fetchPage = 1
        viewModelScope.launch {
            articleRepository.deleteAll()
        }
    }

    private var countDownTimer: CountDownTimer = object : CountDownTimer(3000, 3000) {
        override fun onFinish() {
            searchTerm.value?.let {
                searchArticles(it)
            }
        }

        override fun onTick(millisUntilFinished: Long) {
        }
    }

    //var fetchNewsStatus: MutableLiveData<Resource<NewsResponse>> = MutableLiveData<Resource<NewsResponse>>()
    //var fetchNewsResponse: NewsResponse? = null
    //var lastNewsResponse: NewsResponse? = null

//    fun safeFetchArticles(country: String) {
//        viewModelScope.launch {
//            //fetchNewsStatus.postValue(Resource.Loading())
//            try {
//                Timber.d("#### get top news $country page $fetchPage")
//                val response = articleRepository.getTopNews(country, fetchPage)
//                fetchPage++
//                Timber.d("### safeFetchArticles  response1 ${response}")
//
//                val currNewsResponse = response.body()
//                Timber.d("### safeFetchArticles  response2 ${response.body()}")
//
//                if (response.isSuccessful && currNewsResponse != null) {
//                    if (lastNewsResponse == null) {
//
//                        lastNewsResponse = currNewsResponse
//                    } else {
//                        lastNewsResponse?.articles?.addAll(currNewsResponse.articles)
//                    }
//                    val res = lastNewsResponse ?: currNewsResponse
//                    Timber.d("### safeFetchArticles  Success  ${res}")
//
//                    fetchNewsStatus.postValue(
//                        Resource.Success(res)
//                    )
//                } else {
//                    fetchNewsStatus.postValue(Resource.Failure(message = response.message()))
//                }
//            } catch (throwable: Throwable) {
//                Timber.d("### got error ${throwable.message}")
//                fetchNewsStatus.postValue(Resource.Failure(throwable, throwable.message))
//            }
//        }
}

@Suppress("UNCHECKED_CAST")
class NewsViewModelFactory(val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(application) as T
        }
        throw IllegalArgumentException()
    }

}