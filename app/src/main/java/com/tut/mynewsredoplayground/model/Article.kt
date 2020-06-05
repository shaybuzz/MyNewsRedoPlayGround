package com.tut.mynewsredoplayground.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class Article(
    val author: String?,
    val content: String?,
    val description: String,
    val publishedAt: String,
    @Embedded(prefix = "_source")
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
){
    @PrimaryKey(autoGenerate = true)
    var id:Long? = null
}