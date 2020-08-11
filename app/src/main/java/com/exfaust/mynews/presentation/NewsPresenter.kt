package com.exfaust.mynews.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.exfaust.mynews.api.ApiConstants
import com.exfaust.mynews.data.NewsRepository
import com.exfaust.mynews.data.dataSource.DataSourceFactory
import com.exfaust.mynews.data.dataSource.NewsDataSource
import com.exfaust.mynews.data.model.Article
import com.exfaust.mynews.data.model.NetworkState
import com.exfaust.mynews.presentation.views.MainView
import com.exfaust.mynews.ui.adapters.NewsPagingAdapter
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@InjectViewState
class NewsPresenter @Inject constructor(repository: NewsRepository) :
    MvpPresenter<MainView>(), NewsPagingAdapter.ClickCallback {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var newsAdapter: NewsPagingAdapter =
        NewsPagingAdapter(this)
    private val sourceFactory = DataSourceFactory(compositeDisposable, repository)
    var articlesList: LiveData<PagedList<Article>>

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(ApiConstants.PAGE_SIZE)
            .setInitialLoadSizeHint(ApiConstants.PAGE_SIZE * 2)
            .setPrefetchDistance(ApiConstants.PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()
        articlesList = LivePagedListBuilder<Int, Article>(sourceFactory, config).build()

        viewState.setAdapter(newsAdapter)
    }

    fun getNetworkState(): LiveData<NetworkState> = Transformations.switchMap<NewsDataSource, NetworkState>(
        sourceFactory.dataSourceLiveData
    ) { it.networkState }

    fun getRefreshState(): LiveData<NetworkState> = Transformations.switchMap<NewsDataSource, NetworkState>(
        sourceFactory.dataSourceLiveData
    ) { it.initialLoad }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun onRefresh() {
        //TODO непонятное поведение при обновлении
        sourceFactory.dataSourceLiveData.value!!.invalidate()
    }

    override fun onItemClicked(url: String?) {
        viewState.goToWebView(url!!)
    }

    override fun onClickRetry() {
        sourceFactory.dataSourceLiveData.value!!.retry()
    }
}