package com.bosha.data.repositoriesImpl

import com.bosha.data.api.impl.WeatherDataSource
import com.bosha.domain.common.ErrorResult
import com.bosha.domain.common.Result
import com.bosha.domain.common.SuccessResult
import com.bosha.domain.common.WeatherCoordinates
import com.bosha.domain.entities.CurrentWeather
import com.bosha.domain.repositories.DailyForecastResult
import com.bosha.domain.repositories.HourlyForecastResult
import com.bosha.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val dataSource: WeatherDataSource
) : WeatherRepository {
    override fun getCurrentWeather(weatherCoordinates: WeatherCoordinates): Flow<Result<CurrentWeather>> {
        return dataSource.getCurrentWeatherByLocation(weatherCoordinates)
            .mapToResult()
    }

    override fun getHourlyForecastWeather(weatherCoordinates: WeatherCoordinates): Flow<HourlyForecastResult> {
        return dataSource.getHourlyWeatherByLocation(weatherCoordinates)
            .mapToResult()
    }

    override fun getDailyForecastWeather(weatherCoordinates: WeatherCoordinates): Flow<DailyForecastResult> {
        return dataSource.getDailyWeatherByLocation(weatherCoordinates)
            .mapToResult()
    }

    private fun <T> Flow<T>.mapToResult(): Flow<Result<T>> =
        map { SuccessResult(it) as Result<T> }
            .catch { cause ->
                if ((cause is retrofit2.HttpException) ||
                    (cause is TimeoutException) ||
                    (cause is UnknownHostException)
                )
                    emit(ErrorResult(cause as Exception))
                else throw cause
            }
}