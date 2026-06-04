package com.koksy.appinvest

import android.app.Application
import com.koksy.appinvest.di.AppContainer

class AppInvestApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
