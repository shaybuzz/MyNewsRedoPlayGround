package com.tut.mynewsredoplayground.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tut.mynewsredoplayground.databinding.FragmentSearchNewsBinding
import com.tut.mynewsredoplayground.utils.Resource
import com.tut.mynewsredoplayground.view.adapters.ArticlesListAdapter
import timber.log.Timber

class SearchNewsFragment : Fragment() {

    lateinit var viewModel: NewsViewModel
    private val TAG = SearchNewsFragment::class.java.simpleName
    private lateinit var binding: FragmentSearchNewsBinding
    private lateinit var adapter: ArticlesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)

        //get parent view model
        viewModel = ViewModelProvider(requireActivity()).get(NewsViewModel::class.java)
        binding.viewmodel = viewModel

        initRecyclerView()

        viewModel.searchFetchStatus.observe(viewLifecycleOwner, Observer {
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

        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root

        return binding.root
    }

    private fun initRecyclerView() {
        adapter = ArticlesListAdapter()
        adapter.clickLitener = {
            Timber.d("#### on click article ${it.title}")
            val direction =
                SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleViewFragment(it)
            findNavController().navigate(direction)
        }
        binding.rvSearchNews.adapter = adapter
    }


    private fun showLoader() {
        binding.searchProgress.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.searchProgress.visibility = View.GONE
    }
}