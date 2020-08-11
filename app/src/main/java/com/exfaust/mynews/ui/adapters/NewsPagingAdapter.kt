package com.exfaust.mynews.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.exfaust.mynews.GlideApp
import com.exfaust.mynews.R
import com.exfaust.mynews.data.model.Article
import com.exfaust.mynews.data.model.NetworkState

class NewsPagingAdapter(private val clickCallback: ClickCallback) : PagedListAdapter<Article, RecyclerView.ViewHolder>(
    UserDiffCallback
) {

    private var networkState: NetworkState? = null

    companion object {
        private const val TYPE_ITEMS = 1
        private const val TYPE_FOOTER = 2

        val UserDiffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.publishedAt == newItem.publishedAt
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEMS -> NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false))
            TYPE_FOOTER -> NetworkStateViewHolder.create(
                parent,
                clickCallback
            )
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_ITEMS -> (holder as NewsViewHolder).bind(getItem(position))
            TYPE_FOOTER -> (holder as NetworkStateViewHolder).bindTo(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            TYPE_FOOTER
        } else {
            TYPE_ITEMS
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    /**
     * Set the current network state to the adapter
     * but this work only after the initial load
     * and the adapter already have list to add new loading raw to it
     * so the initial loading state the activity responsible for handle it
     *
     * @param newNetworkState the new network state
     */
    fun setNetworkState(newNetworkState: NetworkState?) {
        if (currentList != null) {
            if (currentList!!.size != 0) {
                val previousState = this.networkState
                val hadExtraRow = hasExtraRow()
                this.networkState = newNetworkState
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState !== newNetworkState) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)
        private val descriptionTextView = itemView.findViewById<TextView>(R.id.descriptionTextView)
        private val dateTextView = itemView.findViewById<TextView>(R.id.dateTextView)
        private val imageView = itemView.findViewById<ImageView>(R.id.imageView)

        fun bind(item: Article?) {
            titleTextView.text = item?.title
            descriptionTextView.text = item?.description
            dateTextView.text = item?.publishedAt

            GlideApp.with(itemView.context)
                .load(item?.urlToImage)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView)

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) clickCallback.onItemClicked(getItem(adapterPosition)?.url)
            }
        }
    }

    interface ClickCallback {
        fun onItemClicked(url: String?)
        fun onClickRetry()
    }
}