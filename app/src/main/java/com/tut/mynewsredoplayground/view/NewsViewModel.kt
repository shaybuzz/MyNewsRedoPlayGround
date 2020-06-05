package com.tut.mynewsredoplayground.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tut.mynewsredoplayground.repositories.ArticleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class NewsViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    val articles = articleRepository.articles

    private var page: Int = 1

    fun fetchArticles() {
        viewModelScope.launch {
            articleRepository.fetch(page = page++)
        }
    }

    fun deleteAll(){
        page = 1
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