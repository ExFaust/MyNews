package com.exfaust.mynews.presentation.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.exfaust.mynews.ui.adapters.NewsPagingAdapter

interface MainView: MvpView{

    fun setAdapter(adapter: NewsPagingAdapter)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun goToWebView(url:String)
}