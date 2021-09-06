package com.bosha.wannaknowweather

import android.app.Application
import com.bosha.wannaknowweather.di.AppComponent
import com.bosha.wannaknowweather.di.DaggerAppComponent

class WeatherApp: Application() {

    lateinit var appComponent: AppComponent private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}