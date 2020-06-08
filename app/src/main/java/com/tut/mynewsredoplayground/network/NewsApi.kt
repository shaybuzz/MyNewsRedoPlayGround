package com.tut.mynewsredoplayground.network

import com.tut.mynewsredoplayground.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//https://newsapi.org/v2/top-headlines?country=us&apiKey=74e4c5c9a4904ac68ca7e90e99a7336f

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getArticles(
        @Query("country") country: String = "us",
        @Query("page") page: Int = 1
    ): NewsResponse

    @GET("v2/top-headlines")
    suspend fun getArticlesRes(
        @Query("country") country: String = "us",
        @Query("page") page: Int = 1
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun search(
        @Query("q") searchNews: String,
        @Query("page") page: Int = 1
    ): NewsResponse


    @GET("v2/top-headlines/{id}")
    suspend fun getParam(
        @Path("id") id: Int? = 66,
        @Query("country") country: String = "us",
        @Query("page") page: Int = 1
    ): Response<NewsResponse>

}