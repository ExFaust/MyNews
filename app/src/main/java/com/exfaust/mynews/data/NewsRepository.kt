package com.exfaust.mynews.data

import com.exfaust.mynews.api.NewsApi
import com.exfaust.mynews.data.model.Article
import com.exfaust.mynews.db.dao.NewsDao
import com.exfaust.mynews.utils.NetHelper
import com.exfaust.mynews.utils.UpdateManager
import com.exfaust.testforskyeng.utils.NoItemException
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api: NewsApi,
    private val netHelper: NetHelper,
    private val newsDao: NewsDao,
    private val updateManager: UpdateManager
) {

    private fun getNewsFromWeb(page: Int): Single<List<Article>> {
        return netHelper.checkConnection()
            .flatMap { api.getNews(page) }
            .flatMap<List<Article>> { response ->
                if (response.status == "ok")
                    //конвертирование времени с апи в читаемый вариант без использования стандартного API28+ способа, туповато, конечно
                    return@flatMap Single.just(response.articles)
                        .flatMapPublisher { Flowable.fromIterable(it) }
                        .map { article ->
                            val pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"
                            val sdf =
                                SimpleDateFormat(pattern, Locale.ENGLISH)
                            val date = sdf.parse(article.publishedAt)
                            val dateFormat =
                                SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.ENGLISH)
                            val newDateString: String = dateFormat.format(date)
                            return@map Article(
                                article.title,
                                article.description,
                                article.urlToImage,
                                article.url,
                                newDateString
                            )
                        }
                        .toList()
                else
                    return@flatMap Single.error { NoItemException() }
            }
    }

    fun getNews(page: Int): Maybe<List<Article>> {
        val newsFromCache: Maybe<List<Article>> = getNewsFromCache()
            .onErrorResumeNext(Maybe.empty())

        val updateMaybe: Maybe<Boolean> = getNewsFromCache()
            .count()
            .flatMapMaybe { count: Long ->
                if (count == 0L || netHelper.isNeedUpdate(updateManager.newsUpdateTimestamp)){
                    return@flatMapMaybe Maybe.just(true)
                } else {
                    return@flatMapMaybe Maybe.just(false)
                }
            }

        val newsFromWeb: Maybe<List<Article>> = updateMaybe
            .flatMap<List<Article>> { isNeedUpdate: Boolean ->
                if (isNeedUpdate) {
                    return@flatMap Maybe.fromSingle(getNewsFromWeb(page))
                } else {
                    return@flatMap Maybe.empty()
                }
            }
            .onErrorResumeNext(Maybe.empty())
            .flatMap { articles ->
                return@flatMap putNewsToCache(articles)
                    .andThen(Maybe.just(articles))
            }

        val maybeList: MutableList<Maybe<List<Article>>> = ArrayList()
        maybeList.add(newsFromCache)
        maybeList.add(newsFromWeb)

        return Maybe.concatDelayError<List<Article>>(maybeList).distinct().firstElement()
    }

    private fun getNewsFromCache(): Maybe<List<Article>> {
        return newsDao.getArticles()
            .subscribeOn(Schedulers.single())
            .onErrorReturnItem(Collections.emptyList())
    }

    private fun putNewsToCache(articles: List<Article>): Completable {
        return newsDao.insertAll(articles)
            .subscribeOn(Schedulers.single())
    }

}