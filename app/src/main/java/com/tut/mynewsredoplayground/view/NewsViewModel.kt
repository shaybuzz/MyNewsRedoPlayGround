package com.tut.mynewsredoplayground.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tut.mynewsredoplayground.repositories.ArticleRepository
import kotlinx.coroutines.launch

class NewsViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    val savedArticles = articleRepository.savedArticles

    val fetchStatus = articleRepository.fetchResponse
    val searchFetchStatus = articleRepository.searchFetchResponse

    private var fetchPage: Int = 1
    private var searchPage: Int = 1


    init {
        fetchArticles("us")
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