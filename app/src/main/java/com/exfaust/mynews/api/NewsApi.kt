package com.exfaust.mynews.api

import com.exfaust.mynews.data.model.NewsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    //запрос отдаёт список новостей, обёрнутый в результат запроса
    @GET("everything?q=android&from=2019-04-00&sortBy=publishedAt&apiKey=26eddb253e7840f988aec61f2ece2907")
    fun getNews(@Query (ApiConstants.PAGE)page:Int): Single<NewsResponse>
}