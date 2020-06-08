package com.tut.mynewsredoplayground.view

import android.app.Application
import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.CountDownTimer
import androidx.lifecycle.*
import com.tut.mynewsredoplayground.model.Article
import com.tut.mynewsredoplayground.repositories.ArticleRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class NewsViewModel(private val articleRepository: ArticleRepository, application:Application) :
    AndroidViewModel(application) {

    val savedArticles = articleRepository.savedArticles

    val fetchStatus = articleRepository.fetchResponse
    val searchFetchStatus = articleRepository.searchFetchResponse

    var searchTerm = MutableLiveData<String>()

    //TODO better then observe forever??
    val isScrolling: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val isScrollingTowardsEndOfList = MutableLiveData<Boolean>().apply {
        observeForever(Observer {
            if (it) {
                Timber.d("#### toward end of list load more ")
                //fetchArticles("us")
            } else {
                Timber.d("#### not toward end")
            }
        })
    }

    private var fetchPage: Int = 1
    private var searchPage: Int = 1


    init {
        fetchArticles("us")
        searchTerm.observeForever(Observer {
            countDownTimer.cancel()
            if (it.isNotEmpty()) {
                countDownTimer.start()
            }
        })
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

    fun fetchArticles(country: String) {
        viewModelScope.launch {
            articleRepository.fetch(country, fetchPage++)
        }
    }

    private fun hasInternetConnection():Boolean{
        val app = getApplication<NewsApp>()
        val cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true

    }

    fun searchArticles(subject: String) {
        viewModelScope.launch {
            articleRepository.search(subject, searchPage++)
        }
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
}

@Suppress("UNCHECKED_CAST")
class NewsViewModelFactory(val articleRepository: ArticleRepository, val application:Application) : ViewModelProvider.AndroidViewModelFactory(application)  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(articleRepository, application) as T
        }
        throw IllegalArgumentException()
    }

}