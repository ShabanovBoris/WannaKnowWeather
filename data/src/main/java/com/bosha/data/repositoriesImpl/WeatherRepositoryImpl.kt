package com.bosha.data.repositoriesImpl

import com.bosha.data.api.impl.WeatherDataSource
import com.bosha.domain.common.ErrorResult
import com.bosha.domain.common.Result
import com.bosha.domain.common.SuccessResult
import com.bosha.domain.common.WeatherCoordinatesLocation
import com.bosha.domain.entities.CurrentWeather
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
    override fun getCurrentWeather(weatherCoordinatesLocation: WeatherCoordinatesLocation): Flow<Result<CurrentWeather>> {
        return dataSource.getCurrentWeatherByLocation(weatherCoordinatesLocation)
            .map { SuccessResult(it) as Result<CurrentWeather> }
            .catch { cause ->
                if ((cause is retrofit2.HttpException) ||
                    (cause is TimeoutException) ||
                    (cause is UnknownHostException)
                )
                    emit(ErrorResult(cause as Exception))

                else throw cause
            }
    }
}