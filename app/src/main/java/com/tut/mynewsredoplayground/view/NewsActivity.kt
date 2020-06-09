package com.tut.mynewsredoplayground.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tut.mynewsredoplayground.R
import com.tut.mynewsredoplayground.database.NewsDatabase
import com.tut.mynewsredoplayground.network.NetworkService
import com.tut.mynewsredoplayground.repositories.ArticleRepositoryImpl
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class NewsActivity : AppCompatActivity() {
    lateinit var newsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        initUi()

        newsViewModel =
            ViewModelProvider(
                this,
                NewsViewModelFactory(application)
            ).get(NewsViewModel::class.java)


        //test()

    }

    private fun test() {
        newsViewModel.deleteAll()
        newsViewModel.fetchArticles("fr")

        testApi()
        testRepo()
    }

    private fun initUi() {
        bottomNavigationView.setupWithNavController(navController.findNavController())
    }

    private fun testRepo() {
        val api = NetworkService.api
        val dao = NewsDatabase.getInstance(this).getNewsDao()
        val repo = ArticleRepositoryImpl(api, dao)
        repo.savedArticles.observe(this, Observer {
            Timber.d("### got \n\n\n $it \n\n\n got size ${it.size}")
        })
        GlobalScope.launch(Dispatchers.IO) {
            repo.fetch("il", 1)
        }
    }

    private fun testApi() {
        GlobalScope.launch(Dispatchers.IO) {
            val res = NetworkService.api.getArticles()
            Timber.d(res.toString())
        }

    }
}