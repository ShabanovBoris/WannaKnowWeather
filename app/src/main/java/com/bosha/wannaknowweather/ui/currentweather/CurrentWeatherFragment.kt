package com.bosha.wannaknowweather.ui.currentweather

import android.content.Context
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
import com.bosha.data.repositoriesImpl.WeatherRepositoryImpl
import com.bosha.domain.common.SuccessResult
import com.bosha.domain.usecases.CurrentWeatherUseCase
import com.bosha.wannaknowweather.BuildConfig
import com.bosha.wannaknowweather.databinding.CurrentWeatherFragmentBinding
import com.bosha.wannaknowweather.utils.injectDeps
import com.bosha.wannaknowweather.utils.viewModelCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Inject


class CurrentWeatherFragment : Fragment() {


    @Inject lateinit var useCase: CurrentWeatherUseCase
    private val viewModel by viewModelCreator<CurrentWeatherViewModel> {
        CurrentWeatherViewModel(useCase)
    }

    /**
     * Called when a fragment is first attached to its context.
     * [.onCreate] will be called after this.
     */
    override fun onAttach(context: Context) {
        injectDeps()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return CurrentWeatherFragmentBinding.inflate(inflater, container, false).run {

            lifecycleScope.launch {
                viewModel.getCurrentWeather().collect {
                    Log.e("TAG", "onCreateView: \n${(it as SuccessResult).data}", )
                }
            }

            return@run root
        }
    }

}