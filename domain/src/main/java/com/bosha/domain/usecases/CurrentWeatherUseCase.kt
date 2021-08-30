package com.bosha.domain.usecases

import com.bosha.domain.common.WeatherLocation
import com.bosha.domain.repositories.WeatherRepository

class CurrentWeatherUseCase(private val repository: WeatherRepository) {

    suspend operator fun invoke(weatherLocation: WeatherLocation) =
        repository.getCurrentWeather(weatherLocation)

}