package com.exfaust.mynews

import android.app.Application
import com.exfaust.mynews.di.components.AppComponent
import com.exfaust.mynews.di.components.DaggerAppComponent
import com.exfaust.mynews.di.components.NewsComponent
import com.exfaust.mynews.di.modules.ApplicationModule
import com.exfaust.mynews.di.modules.MainModule
import com.exfaust.mynews.di.modules.NetModule
import com.exfaust.mynews.di.modules.NewsFragmentModule
import com.exfaust.mynews.ui.adapters.NewsPagingAdapter

class App : Application(){

    private lateinit var component: AppComponent
    private var newsComponent: NewsComponent? = null

    override fun onCreate() {
        super.onCreate()

        instance = this
        setup()
    }

    fun initNewsComponent(clickCallback: NewsPagingAdapter.ClickCallback): NewsComponent? {
        newsComponent = component.newsComponent(NewsFragmentModule(clickCallback))
        return newsComponent
    }

    fun destroyNewsComponent() {
        newsComponent = null
    }

    private fun setup() {
        component = DaggerAppComponent.builder()
            .applicationModule(
                ApplicationModule(
                    this
                )
            )
            .netModule(NetModule())
            .mainModule(MainModule())
            .build()
        component.inject(this)
    }

    fun getAppComponent(): AppComponent {
        return component
    }

    companion object {
        lateinit var instance: App private set
    }
}