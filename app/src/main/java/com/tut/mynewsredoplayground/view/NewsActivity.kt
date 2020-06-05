package com.tut.mynewsredoplayground.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
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

        val api = NetworkService.api
        val dao = NewsDatabase.getInstance(this).getNewsDao()
        val repo = ArticleRepositoryImpl(api, dao)
        newsViewModel =
            ViewModelProvider(this, NewsViewModelFactory(repo)).get(NewsViewModel::class.java)

//        newsViewModel.deleteAll()
//        newsViewModel.fetchArticles()

        //testApi()
        //testRepo()
    }

    private fun initUi() {
        NavigationUI.setupWithNavController(bottomNavigationView, findNavController(R.id.fragment))
    }

    private fun testRepo() {
        val api = NetworkService.api
        val dao = NewsDatabase.getInstance(this).getNewsDao()
        val repo = ArticleRepositoryImpl(api, dao)
        repo.articles.observe(this, Observer {
            Timber.d("### got \n\n\n $it \n\n\n got size ${it.size}")
        })
        GlobalScope.launch(Dispatchers.IO) {
            repo.fetch()
        }
    }

    private fun testApi() {
        GlobalScope.launch(Dispatchers.IO) {
            val res = NetworkService.api.getArticles()
            Timber.d(res.toString())
        }

    }
}