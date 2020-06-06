package com.tut.mynewsredoplayground.view.adapters

import android.graphics.drawable.Drawable
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tut.mynewsredoplayground.model.Article


object BindingAdapters {

    @JvmStatic
    @BindingAdapter("app:loadImage")
    fun loadImage(imageView: ImageView, url: String?) {
        url?.let {
            Glide.with(imageView).load(url).into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("setArticleTitle")
    fun TextView.setArticle(item: Article) {
        text = item.title
    }


    @JvmStatic
    @BindingAdapter(value = ["imageFromUrl", "placeHolderer"], requireAll = true)
    fun bindurl(image: ImageView, imageUrl: String?, thePlaceholder: Drawable?) {
        if (imageUrl != null && imageUrl.isNotEmpty() && thePlaceholder != null)
            Glide.with(image.context).load(imageUrl).placeholder(thePlaceholder).transition(
                DrawableTransitionOptions.withCrossFade()
            ).into(image)
    }

    @JvmStatic
    @BindingAdapter("app:isScrollingg")
    fun islistScrolling(
        recyclerView: RecyclerView,
        isScrolling: MutableLiveData<Boolean>?
    ) {
        isScrolling?.postValue(true)
//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//            }
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                isScrolling?.postValue(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
//            }
//        })
    }


    @JvmStatic
    @BindingAdapter(
        value = ["app:isScrolledTowardEndOfList", "app:isScrolling"],
        requireAll = false
    )
    fun isTodfdfwardsEndOfList(
        recyclerView: RecyclerView,
        isTowardEndOfList: MutableLiveData<Boolean>?,
        isScrolling: MutableLiveData<Boolean>?
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisible = linearLayoutManager.findLastVisibleItemPosition()
                val visibleItemsCount = linearLayoutManager.childCount
                val totalListItemsCount = linearLayoutManager.itemCount
                val preLastPageItemPosition = totalListItemsCount - visibleItemsCount
                isTowardEndOfList?.postValue(lastVisible >= preLastPageItemPosition)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolling?.postValue(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
            }
        })
    }
}