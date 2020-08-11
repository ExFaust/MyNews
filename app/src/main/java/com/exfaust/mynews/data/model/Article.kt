package com.exfaust.mynews.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Article(
    @PrimaryKey
    val title: String,
    val description: String,
    val urlToImage: String?,
    val url: String,
    val publishedAt: String)