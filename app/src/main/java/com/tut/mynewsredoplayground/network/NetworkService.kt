package com.tut.mynewsredoplayground.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

private const val API_PARAM = "apiKey"
private const val API_KEY = "74e4c5c9a4904ac68ca7e90e99a7336f"
private const val BASE_URL = "https://newsapi.org"

class NetworkService  {
    companion object {
        private val retrofit by lazy {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Timber.tag("okhttp").d(message)
                    }
                }).apply { level = HttpLoggingInterceptor.Level.BODY }
            val paramInterceptor = Interceptor({
                val url =
                    it.request().url.newBuilder().addQueryParameter(API_PARAM, API_KEY).build()
                val newRequest = it.request().newBuilder().url(url).build()
                it.proceed(newRequest)
            })

            val okHttpClient = OkHttpClient().newBuilder().addInterceptor(httpLoggingInterceptor)
                .addInterceptor(paramInterceptor).build()

            Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }

        val api by lazy {
            retrofit.create(NewsApi::class.java)
        }

    }
}