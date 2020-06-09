package com.tut.mynewsredoplayground.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.tut.mynewsredoplayground.databinding.FragmentArticleBinding
import kotlinx.android.synthetic.main.fragment_article.*

//class ArticleViewFragment : Fragment(R.layout.fragment_article) {
class ArticleViewFragment : Fragment() {


    val arg: ArticleViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentArticleBinding.inflate(inflater, container, false)
        binding.article = arg.article
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = arg.article
        val viewModel by activityViewModels<NewsViewModel>()
        webView.apply {
            val webClient = WebViewClient()
            loadUrl(article.url)
        }
        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(fab, "Article saved", Snackbar.LENGTH_SHORT).show()
        }
    }
}