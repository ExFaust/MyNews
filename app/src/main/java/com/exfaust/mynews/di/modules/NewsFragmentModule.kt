package com.exfaust.mynews.di.modules

import android.view.LayoutInflater
import com.exfaust.mynews.di.components.NewsScope
import com.exfaust.mynews.ui.adapters.NewsPagingAdapter
import dagger.Module
import dagger.Provides


@Module
class NewsFragmentModule(private val clickCallback: NewsPagingAdapter.ClickCallback) {

    @NewsScope
    @Provides
    fun provideListAdapter(
        layoutInflater: LayoutInflater
    ): NewsPagingAdapter {
        return NewsPagingAdapter(clickCallback, layoutInflater)
    }
}