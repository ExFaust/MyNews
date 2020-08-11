package com.exfaust.mynews.di

import com.exfaust.mynews.App
import com.exfaust.mynews.di.modules.ApplicationModule
import com.exfaust.mynews.di.modules.DbModule
import com.exfaust.mynews.di.modules.MainModule
import com.exfaust.mynews.di.modules.NetModule
import com.exfaust.mynews.ui.fragments.NewsFragment
import com.exfaust.mynews.ui.fragments.WebViewFragment
import dagger.Component

@Component(modules = [ApplicationModule::class, NetModule::class, MainModule::class, DbModule::class])
interface AppComponent {

    fun inject(application: App)
    fun inject(newsFragment: NewsFragment)
    fun inject(webViewFragment: WebViewFragment)
}