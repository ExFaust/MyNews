package com.exfaust.mynews.di.components

import com.exfaust.mynews.App
import com.exfaust.mynews.di.modules.*
import com.exfaust.mynews.ui.fragments.WebViewFragment
import dagger.Component

@Component(modules = [ApplicationModule::class, NetModule::class, MainModule::class, DbModule::class])
interface AppComponent {

    fun newsComponent(newsFragmentModule: NewsFragmentModule): NewsComponent

    fun inject(application: App)
    fun inject(webViewFragment: WebViewFragment)
}