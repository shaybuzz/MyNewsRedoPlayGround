package com.tut.mynewsredoplayground.view.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tut.mynewsredoplayground.model.Article

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("app:loadImage")
    fun loadImage(imageView: ImageView, url:String?){
        url?.let {
            Glide.with(imageView).load(url).into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("setArticleTitle")
    fun TextView.setArticle(item: Article) {
        text = item.title
    }
}