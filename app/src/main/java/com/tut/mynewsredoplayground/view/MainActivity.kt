package com.tut.mynewsredoplayground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tut.mynewsredoplayground.network.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        //testApi()
    }

    private fun testApi() {
        GlobalScope.launch(Dispatchers.IO) {
           val res = NetworkService.api.getArticles()
            Timber.d(res.toString())
        }

    }
}