package com.exfaust.mynews

import android.app.Application
import com.exfaust.mynews.di.AppComponent
import com.exfaust.mynews.di.DaggerAppComponent
import com.exfaust.mynews.di.modules.ApplicationModule
import com.exfaust.mynews.di.modules.MainModule
import com.exfaust.mynews.di.modules.NetModule

class App : Application(){

    private lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        instance = this
        setup()
    }

    fun setup() {
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