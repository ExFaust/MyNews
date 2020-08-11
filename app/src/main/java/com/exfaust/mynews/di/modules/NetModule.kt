package com.exfaust.mynews.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.exfaust.mynews.api.ApiConstants
import com.exfaust.mynews.api.NewsApi
import com.exfaust.mynews.utils.NetHelper
import com.exfaust.mynews.utils.UpdateManager
import com.exfaust.mynews.utils.UpdateManagerImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetModule{

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)

       return builder
            .build()
    }

    @Provides
    fun provideApi(retrofitBuilder: Retrofit.Builder): NewsApi {
        return retrofitBuilder
            .baseUrl(ApiConstants.BASE_URL)
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    fun provideRetrofitBuilder(client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
    }

    @Provides
    fun provideNetHelper(context: Context): NetHelper {
        return NetHelper(context)
    }

    @Provides
    fun provideUpdateManager(sharedPreferences: SharedPreferences): UpdateManager {
        return UpdateManagerImpl(sharedPreferences)
    }
}