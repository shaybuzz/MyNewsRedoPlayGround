package com.tut.mynewsredoplayground.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tut.mynewsredoplayground.databinding.TopNewsBinding
import com.tut.mynewsredoplayground.view.adapters.ArticlesListAdapter
import timber.log.Timber

class TopNewsFragment : Fragment() {

    private val TAG = TopNewsFragment::class.java.simpleName
    private lateinit var binding: TopNewsBinding
    private lateinit var adapter: ArticlesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TopNewsBinding.inflate(inflater, container, false)
        val viewModel by activityViewModels<NewsViewModel>()
        binding.viewmodel = viewModel

        initRecyclerView()

        viewModel.articles.observe(viewLifecycleOwner, Observer {
            Timber.d("#### fragmebnt got articiles ")
            adapter.submitItems(it)
        })

//        viewModel.isScrollingTowardsEndOfList.observe(viewLifecycleOwner, Observer {
//            if(it){
//                viewModel.safeFetchArticles("us")
//            }
//        })

        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = ArticlesListAdapter()
        adapter.clickLitener = {
            Timber.d("#### on click article ${it.title}")
            val direction = TopNewsFragmentDirections.actionTopNewsFragmentToArticleViewFragment(it)
            findNavController().navigate(direction)
        }


        binding.topNews.adapter = adapter
    }

    //moved to data binding added listener to list
    private fun checkScroll() {
        binding.topNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (isScrollToEnd(recyclerView.layoutManager as LinearLayoutManager)) {
                    Timber.d("### scroll down...")
                } else {
                    Timber.d("### scroll ...")
                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    Timber.d("Scrolling")
                } else {
                    Timber.d("not Scrolling")
                }
            }
        })

    }

    private fun isScrollToEnd(linearLayoutManager: LinearLayoutManager): Boolean {
        val lastVisible = linearLayoutManager.findLastVisibleItemPosition()
        val visibleItemsCount = linearLayoutManager.childCount
        val totalListItemsCount = linearLayoutManager.itemCount
        val preLastPageItemPosition = totalListItemsCount - visibleItemsCount
        return lastVisible >= preLastPageItemPosition
    }


    private fun showLoader() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.paginationProgressBar.visibility = View.GONE
    }
}