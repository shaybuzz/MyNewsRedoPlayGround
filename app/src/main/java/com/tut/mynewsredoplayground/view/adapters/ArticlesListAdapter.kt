package com.tut.mynewsredoplayground.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tut.mynewsredoplayground.databinding.ItemArticlePreviewBinding
import com.tut.mynewsredoplayground.model.Article

class ArticlesListAdapter : RecyclerView.Adapter<ArticlesListAdapter.ArticleViewHolder>() {

    val diffCallBack = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    private val items = AsyncListDiffer<Article>(this, diffCallBack)

    fun submitItems(data: List<Article>) {
        items.submitList(data)
    }

    inner class ArticleViewHolder(val binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =
            ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = items.currentList.get(position)
        holder.binding.article = article
        holder.binding.executePendingBindings()
    }

}