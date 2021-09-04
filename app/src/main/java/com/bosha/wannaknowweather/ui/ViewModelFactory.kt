package com.bosha.wannaknowweather.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bosha.domain.usecases.CurrentWeatherUseCase
import com.bosha.wannaknowweather.di.scopes.ScreenScope
import com.bosha.wannaknowweather.ui.currentweather.CurrentWeatherViewModel
import javax.inject.Inject

@ScreenScope
class ViewModelFactory @Inject constructor(
    private val currentWeatherUseCase: CurrentWeatherUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {

        CurrentWeatherViewModel::class.java -> CurrentWeatherViewModel(currentWeatherUseCase)

        else -> error("$modelClass is not registered ViewModel")
    } as T
}
