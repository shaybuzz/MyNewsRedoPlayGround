package com.tut.mynewsredoplayground.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.tut.mynewsredoplayground.databinding.TopNewsBinding
import timber.log.Timber

class TopNewsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = TopNewsBinding.inflate(inflater, container, false)
        val viewModel by activityViewModels<NewsViewModel>()
        viewModel.articles.observe(viewLifecycleOwner, Observer {
            Timber.d("###Fragment got ${it.size} ")
        })
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}