package com.tut.mynewsredoplayground.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tut.mynewsredoplayground.databinding.ItemArticlePreviewBinding
import com.tut.mynewsredoplayground.model.Article

class ArticlesListAdapter : RecyclerView.Adapter<ArticlesListAdapter.ArticleViewHolder>() {

    var clickLitener: ((Article) -> Unit)? = null

    private val diffCallBack = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    private val differItems = AsyncListDiffer<Article>(this, diffCallBack)

    fun submitItems(data: List<Article>) {
        differItems.submitList(data)
    }

    inner class ArticleViewHolder(val binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =
            ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differItems.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differItems.currentList.get(position)
        holder.binding.article = article
        clickLitener?.let { clickListener ->
            holder.binding.root.setOnClickListener {
                clickListener.invoke(article)
            }
        }
        holder.binding.executePendingBindings()
    }

}