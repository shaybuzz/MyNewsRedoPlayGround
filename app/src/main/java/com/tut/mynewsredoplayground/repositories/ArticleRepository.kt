package com.tut.mynewsredoplayground.repositories

import androidx.lifecycle.LiveData
import com.tut.mynewsredoplayground.model.Article
import com.tut.mynewsredoplayground.utils.Resource

interface ArticleRepository {

    val savedArticles: LiveData<List<Article>>

    val fetchResponse: LiveData<Resource<List<Article>>>
    suspend fun fetch(country: String, page: Int = 1)

    suspend fun deleteAll()

}