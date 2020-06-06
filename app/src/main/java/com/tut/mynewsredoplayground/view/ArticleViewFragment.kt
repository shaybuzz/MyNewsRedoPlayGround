package com.tut.mynewsredoplayground.view

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.tut.mynewsredoplayground.R
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleViewFragment : Fragment(R.layout.fragment_article) {

    val arg: ArticleViewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = arg.article
        webView.apply {
            val webClient = WebViewClient()
            loadUrl(article.url)
        }
    }
}