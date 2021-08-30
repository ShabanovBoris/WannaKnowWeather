package com.bosha.wannaknowweather

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

object Api{
    private val TAG = MainActivity::class.java.simpleName
    private const val API_KEY = BuildConfig.THE_WEATHER_APIKEY
    private const val BASEURL = BuildConfig.BASE_URL
}

class MainActivity : AppCompatActivity(R.layout.activity_main)
