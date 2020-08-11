package com.exfaust.mynews.presentation

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.exfaust.mynews.presentation.views.WebFragmentView
import javax.inject.Inject

@InjectViewState
class WebViewPresenter @Inject constructor() : MvpPresenter<WebFragmentView>() {

    fun onGetUrl(url:String){
        viewState.loadUrl(url)
    }

    fun onLoadedWebView(){
        viewState.hideLoading()
    }

    fun onProgressChangedWebView(progress:Int){
        viewState.showLoading(progress)
    }
}