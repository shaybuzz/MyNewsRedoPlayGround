package com.tut.mynewsredoplayground.repositories

import androidx.lifecycle.LiveData
import com.tut.mynewsredoplayground.model.Article

interface ArticleRepository {

    val articles:LiveData<List<Article>>

    suspend fun fetch(page:Int=1)

    suspend fun deleteAll()

}