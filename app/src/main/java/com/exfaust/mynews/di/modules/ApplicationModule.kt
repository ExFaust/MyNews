package com.exfaust.mynews.di.modules

import android.app.Application
import android.content.Context
import com.exfaust.mynews.App
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
}