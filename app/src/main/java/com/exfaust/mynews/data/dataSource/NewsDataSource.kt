package com.exfaust.mynews.data.dataSource

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.exfaust.mynews.data.NewsRepository
import com.exfaust.mynews.data.model.Article
import com.exfaust.mynews.data.model.NetworkState
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class NewsDataSource(
    private val newsRepository: NewsRepository,
    private val compositeDisposable: CompositeDisposable
) : ItemKeyedDataSource<Int, Article>() {

    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    /**
     * Keep Completable reference for the retry event
     */
    private var retryCompletable: Completable? = null

    private var pageNumber = 1

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Article>
    ) {
        // update network states.
        // provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        compositeDisposable.add(newsRepository.getNews(1)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {throwable ->
                // keep a Completable for future retry
                setRetry(Action { loadInitial(params, callback) })
                val error = NetworkState.error(throwable)
                // publish the error
                networkState.postValue(error)
                initialLoad.postValue(error)
            }
            .onErrorResumeNext (Maybe.never())
            .doOnSuccess{ articles ->
                // clear retry since last request succeeded
                setRetry(null)
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                callback.onResult(articles)
                pageNumber++
            }
            .subscribe()
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Article>) {
        // set network value to loading.
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(newsRepository.getNews(params.key)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError{ throwable ->
                // keep a Completable for future retry
                setRetry(Action { loadAfter(params, callback) })
                // publish the error
                networkState.postValue(NetworkState.error(throwable))
            }
            .onErrorResumeNext (Maybe.never())
            .doOnSuccess {articles ->
                // clear retry since last request succeeded
                setRetry(null)
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(articles)
                pageNumber++ }
            .subscribe()
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Article>) {
        // ignored, since we only ever append to our initial load
    }

    override fun getKey(item: Article): Int {
        return pageNumber
    }

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }

}