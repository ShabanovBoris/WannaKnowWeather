package com.bosha.wannaknowweather.ui.currentweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bosha.data.api.WeatherApi
import com.bosha.data.dto.Response
import com.bosha.wannaknowweather.BuildConfig
import com.bosha.wannaknowweather.R
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


class CurrentWeatherFragment : Fragment() {

    val api = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .client(
            OkHttpClient().newBuilder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY))
                .addNetworkInterceptor(HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        )
        .build()
        .create(WeatherApi::class.java)




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.current_weather_fragment, container, false)
        fun getInfo() {
            lifecycleScope.launch {
                val temp = api.currentWeather().current.temp.toString()
                val dt = api.currentWeather().current.dt


                val time = dt * 1000.toLong()
                val date = Date(time)
                val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy a")
                format.timeZone = TimeZone.getTimeZone("GMT")

                view.findViewById<TextView>(R.id.testView).apply {
                    post {
                        text = "$temp + ${format.format(date)}"
                    }
                }
            }
        }

        getInfo()
        return view
    }

}