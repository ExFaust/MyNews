package com.exfaust.mynews.di.components

import com.exfaust.mynews.di.modules.NewsFragmentModule
import com.exfaust.mynews.ui.fragments.NewsFragment
import dagger.Subcomponent

@NewsScope
@Subcomponent(modules = [NewsFragmentModule::class])
interface NewsComponent {
    fun inject(newsFragment: NewsFragment)
}
