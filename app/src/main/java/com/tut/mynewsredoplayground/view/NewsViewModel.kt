package com.tut.mynewsredoplayground.view

import android.os.CountDownTimer
import androidx.lifecycle.*
import com.tut.mynewsredoplayground.model.Article
import com.tut.mynewsredoplayground.repositories.ArticleRepository
import kotlinx.coroutines.launch

class NewsViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    val savedArticles = articleRepository.savedArticles

    val fetchStatus = articleRepository.fetchResponse
    val searchFetchStatus = articleRepository.searchFetchResponse

    var searchTerm = MutableLiveData<String>()


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

    fun saveArticle(article: Article){
        viewModelScope.launch {
            articleRepository.saveArticle(article)
        }
    }

    fun unSaveArticle(article: Article){
        viewModelScope.launch {
            articleRepository.unSaveArticle(article)
        }
    }

    fun fetchArticles(country: String) {
        viewModelScope.launch {
            articleRepository.fetch(country, fetchPage++)
        }
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
class NewsViewModelFactory(val articleRepository: ArticleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(articleRepository) as T
        }
        throw IllegalArgumentException()
    }

}