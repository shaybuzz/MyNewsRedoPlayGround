package com.tut.mynewsredoplayground.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tut.mynewsredoplayground.databinding.FragmentSavedNewsBinding

class SavedNewsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        val viewModel by activityViewModels<NewsViewModel>()

        return binding.root
    }
}