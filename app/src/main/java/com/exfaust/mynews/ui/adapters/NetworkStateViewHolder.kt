package com.exfaust.mynews.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exfaust.mynews.R
import com.exfaust.mynews.data.model.NetworkState
import com.exfaust.mynews.data.model.Status
import com.exfaust.mynews.utils.NoConnectivityException
import kotlinx.android.synthetic.main.network_state_item.view.*
import retrofit2.HttpException

class NetworkStateViewHolder(
    view: View,
    private val retryCallback: NewsPagingAdapter.ClickCallback
) : RecyclerView.ViewHolder(view) {

    init {
        itemView.retryLoadingButton.setOnClickListener { retryCallback.onClickRetry() }
    }

    fun bindTo(networkState: NetworkState?) {
        //error message
        itemView.errorMessageTextView.visibility =
            if (networkState?.throwable != null) View.VISIBLE else View.GONE
        if (networkState?.throwable != null) {
            when (networkState.throwable) {
                is NoConnectivityException -> itemView.errorMessageTextView.setText(R.string.no_connectivity_message)
                is HttpException -> itemView.errorMessageTextView.setText(R.string.http_message)
                else -> itemView.errorMessageTextView.setText(R.string.error_message)
            }
        }

        //loading and retry
        itemView.retryLoadingButton.visibility =
            if (networkState?.status == Status.FAILED) View.VISIBLE else View.GONE
        itemView.loadingProgressBar.visibility =
            if (networkState?.status == Status.RUNNING) View.VISIBLE else View.GONE
    }

    companion object {
        fun create(
            parent: ViewGroup,
            retryCallback: NewsPagingAdapter.ClickCallback,
            layoutInflater: LayoutInflater
        ): NetworkStateViewHolder {
            val view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateViewHolder(
                view,
                retryCallback
            )
        }
    }

}