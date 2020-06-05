package com.tut.mynewsredoplayground.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.tut.mynewsredoplayground.databinding.TopNewsBinding
import com.tut.mynewsredoplayground.utils.Resource
import com.tut.mynewsredoplayground.view.adapters.ArticlesListAdapter
import timber.log.Timber

class TopNewsFragment : Fragment() {

    private val TAG = TopNewsFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = TopNewsBinding.inflate(inflater, container, false)
        val viewModel by activityViewModels<NewsViewModel>()
        val adapter = ArticlesListAdapter()
        adapter.clickLitener = {
            Timber.d("#### on click article ${it.title}")
        }

        binding.topNews.adapter = adapter

        viewModel.fetchStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    adapter.submitItems(it.data)
                }
                is Resource.Loading -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE
                    //it.partialData
                }
                is Resource.Failure -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    Log.e(TAG, it.message)
                }
            }
        })

        viewModel.savedArticles.observe(viewLifecycleOwner, Observer {
            adapter.submitItems(it)
        })


        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}