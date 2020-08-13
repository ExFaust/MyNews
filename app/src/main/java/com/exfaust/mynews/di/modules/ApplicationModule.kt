package com.exfaust.mynews.di.modules

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import com.exfaust.mynews.App
import com.exfaust.mynews.ui.fragments.NewsFragment
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ApplicationModule(private val application: App) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

    @Provides
    fun provideContext(): Context {
        return application
    }

    @Provides
    fun provideLayoutInflater(context: Context): LayoutInflater {
        return LayoutInflater.from(context)
    }

    @Provides
    fun provideNewsFragment(): NewsFragment {
        return NewsFragment()
    }
}