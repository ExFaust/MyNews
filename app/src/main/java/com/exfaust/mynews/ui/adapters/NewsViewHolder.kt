package com.exfaust.mynews.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exfaust.mynews.GlideApp
import com.exfaust.mynews.R
import com.exfaust.mynews.data.model.Article
import kotlinx.android.synthetic.main.news_item.view.*

class NewsViewHolder(
    view: View,
    private val clickCallback: NewsPagingAdapter.ClickCallback
) : RecyclerView.ViewHolder(view) {

    fun bindTo(item: Article?) {
        itemView.titleTextView.text = item?.title
        itemView.descriptionTextView.text = item?.description
        itemView.dateTextView.text = item?.publishedAt

        GlideApp.with(itemView.context)
            .load(item?.urlToImage)
            .placeholder(R.drawable.ic_launcher_background)
            .into(itemView.imageView)

        if (adapterPosition != RecyclerView.NO_POSITION) clickCallback.onItemClicked(item?.url)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            clickCallback: NewsPagingAdapter.ClickCallback,
            layoutInflater: LayoutInflater
        ): NewsViewHolder {
            val view = layoutInflater.inflate(R.layout.news_item, parent, false)
            return NewsViewHolder(
                view,
                clickCallback
            )
        }
    }

}
