package com.exfaust.mynews.di.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.exfaust.mynews.api.NewsApi
import com.exfaust.mynews.data.NewsRepository
import com.exfaust.mynews.db.dao.NewsDao
import com.exfaust.mynews.utils.NetHelper
import com.exfaust.mynews.utils.UpdateManager
import dagger.Module
import dagger.Provides


@Module
class MainModule {

    @Provides
    fun provideSearchRepository(api: NewsApi, netHelper: NetHelper, newsDao: NewsDao, updateManager: UpdateManager): NewsRepository {
        return NewsRepository(api,netHelper,newsDao,updateManager)
    }

    @Provides
    fun providesSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}