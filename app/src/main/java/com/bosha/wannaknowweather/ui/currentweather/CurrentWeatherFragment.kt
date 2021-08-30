package com.bosha.wannaknowweather.ui.currentweather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bosha.data.api.WeatherApi
import com.bosha.data.api.impl.WeatherDataSource
import com.bosha.data.mappers.WeatherResponseMapper
import com.bosha.data.repositories.WeatherRepositoryImpl
import com.bosha.domain.usecases.CurrentWeatherUseCase
import com.bosha.wannaknowweather.BuildConfig
import com.bosha.wannaknowweather.databinding.CurrentWeatherFragmentBinding
import com.bosha.wannaknowweather.utils.viewModelCreator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class CurrentWeatherFragment : Fragment() {

    val viewModel by viewModelCreator<CurrentWeatherViewModel> {
        CurrentWeatherViewModel(
            CurrentWeatherUseCase(
                WeatherRepositoryImpl(
                    WeatherDataSource(api, WeatherResponseMapper())
                )
            )
        )

    }


    val api = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .client(
            OkHttpClient().newBuilder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .addNetworkInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .build()
        )
        .build()
        .create(WeatherApi::class.java)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return CurrentWeatherFragmentBinding.inflate(inflater, container, false).run {

            lifecycleScope.launch {
                viewModel.weatherFlow.collect {
                    Log.e("TAG", "onCreateView: \n${it}", )
                }
            }

            return@run root
        }
    }

}