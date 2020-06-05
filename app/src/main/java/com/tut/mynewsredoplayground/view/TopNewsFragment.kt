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
    private lateinit var binding: TopNewsBinding
    private lateinit var adapter:ArticlesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TopNewsBinding.inflate(inflater, container, false)
        val viewModel by activityViewModels<NewsViewModel>()

        initRecyclerView()

        viewModel.fetchStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideLoader()
                    adapter.submitItems(it.data)
                }
                is Resource.Loading -> {
                    showLoader()
                    //it.partialData
                }
                is Resource.Failure -> {
                    hideLoader()
                    Log.e(TAG, it.message)
                }
            }
        })

//        viewModel.savedArticles.observe(viewLifecycleOwner, Observer {
//            adapter.submitItems(it)
//        })


        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = ArticlesListAdapter()
        adapter.clickLitener = {
            Timber.d("#### on click article ${it.title}")
        }

        binding.topNews.adapter = adapter
    }


    private fun showLoader() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.paginationProgressBar.visibility = View.GONE
    }
}