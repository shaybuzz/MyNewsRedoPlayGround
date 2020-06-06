package com.tut.mynewsredoplayground.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tut.mynewsredoplayground.databinding.FragmentSavedNewsBinding
import com.tut.mynewsredoplayground.view.adapters.ArticlesListAdapter


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

        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val article = adapter.getItem(viewHolder.adapterPosition)
                article?.let {
                    viewModel.unSaveArticle(it)
                    Snackbar.make(binding.rvSavedNews, "Article deleted", Snackbar.LENGTH_SHORT)
                        .apply {
                            setAction("Undo", object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    viewModel.saveArticle(article)
                                }
                            })
                            show()
                        }
                }
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.rvSavedNews)



        adapter.clickLitener = {
            val direction =
                SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleViewFragment(it)
            findNavController().navigate(direction)
        }
        binding.rvSavedNews.adapter = adapter
    }
}