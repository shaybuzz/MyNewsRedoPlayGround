package com.tut.mynewsredoplayground.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tut.mynewsredoplayground.R
import com.tut.mynewsredoplayground.databinding.FragmentSavedNewsBinding
import com.tut.mynewsredoplayground.utils.Consts
import com.tut.mynewsredoplayground.view.adapters.ArticlesListAdapter
import timber.log.Timber

class SavedNewsFragment : Fragment() {

    lateinit var viewModel: NewsViewModel
    private val TAG = SavedNewsFragment::class.java.simpleName
    private lateinit var binding: FragmentSavedNewsBinding
    private lateinit var adapter: ArticlesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)

        //get parent view model
        viewModel = ViewModelProvider(requireActivity()).get(NewsViewModel::class.java)
        //binding.viewmodel = viewModel

        initRecyclerView()

        viewModel.savedArticles.observe(viewLifecycleOwner, Observer {
            adapter.submitItems(it)
        })

        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root

        return binding.root
    }

    private fun initRecyclerView() {
        adapter = ArticlesListAdapter()
        adapter.clickLitener = {
            val direction =
                SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleViewFragment(it)
            findNavController().navigate(direction)
        }
        binding.rvSavedNews.adapter = adapter
    }
}