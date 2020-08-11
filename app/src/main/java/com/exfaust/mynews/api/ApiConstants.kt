package com.exfaust.mynews.api

object ApiConstants {

        const val BASE_URL = "https://newsapi.org/v2/"
        const val PAGE = "page"
        const val PAGE_LIMIT = 5 //для хардкодного ограничения кол-ва страниц в запросе
        const val PAGE_SIZE = 5 //кол-во загружаемых для отображения элементов, а также префетчинга
}