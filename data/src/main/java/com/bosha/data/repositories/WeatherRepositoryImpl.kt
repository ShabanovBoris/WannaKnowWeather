package com.bosha.data.repositories

import com.bosha.data.api.impl.WeatherDataSource
import com.bosha.domain.common.WeatherLocation
import com.bosha.domain.entities.CurrentWeather
import com.bosha.domain.repositories.WeatherRepository

class WeatherRepositoryImpl(
    private val dataSource: WeatherDataSource
): WeatherRepository {
    override suspend fun getCurrentWeather(weatherLocation: WeatherLocation): CurrentWeather {
        return dataSource.getCurrentWeatherByLocation(weatherLocation)
    }
}