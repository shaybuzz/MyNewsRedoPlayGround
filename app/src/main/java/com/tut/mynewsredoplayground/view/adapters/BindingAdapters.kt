package com.tut.mynewsredoplayground.view.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("app:loadImage")
    fun loadImage(imageView: ImageView, url:String?){
        url?.let {
            Glide.with(imageView).load(url).into(imageView)
        }

    }
}