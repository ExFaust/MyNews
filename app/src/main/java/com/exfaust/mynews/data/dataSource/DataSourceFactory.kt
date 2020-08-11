package com.exfaust.mynews.data.dataSource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.exfaust.mynews.data.NewsRepository
import com.exfaust.mynews.data.model.Article
import io.reactivex.disposables.CompositeDisposable


class DataSourceFactory(private val compositeDisposable: CompositeDisposable,
                             private val newsRepository: NewsRepository)
    : DataSource.Factory<Int, Article>() {

    val dataSourceLiveData = MutableLiveData<NewsDataSource>()

    override fun create(): DataSource<Int, Article> {

        val dataSource = NewsDataSource(newsRepository, compositeDisposable)
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }

}