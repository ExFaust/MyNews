package com.exfaust.mynews.presentation.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface WebFragmentView : MvpView{

    fun loadUrl(url:String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showLoading(progress:Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideLoading()
}