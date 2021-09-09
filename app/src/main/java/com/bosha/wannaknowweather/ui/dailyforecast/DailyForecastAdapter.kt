package com.bosha.wannaknowweather.ui.dailyforecast

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bosha.data.api.WeatherConstants
import com.bosha.domain.entities.DailyForecast
import com.bosha.wannaknowweather.R
import com.bosha.wannaknowweather.databinding.DailyForecastItemBinding
import com.bosha.wannaknowweather.utils.unixSecondsToDate
import com.bosha.wannaknowweather.utils.unixSecondsToDayOfWeek
import kotlin.math.roundToInt


class DailyForecastAdapter :
    RecyclerView.Adapter<DailyForecastAdapter.DailyForecastViewHolder>() {

    var forecastList: List<DailyForecast> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder =
        DailyForecastViewHolder(
            DailyForecastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        val item = forecastList[position]
        holder.binding.apply {
//            tvTime.text = forecastList[position].dt.unixSecondsToTime()
//            tvTemp.text = forecastList[position].temp.roundToInt().toString()
//            tvTemp.append(tvTemp.context.getString(R.string.celsius_sign))
//            ivIconWeather.load(WeatherConstants.getImageUrl(forecastList[position].weather[0].icon))
//            tvWindSpeed.text = forecastList[position].windSpeed.toString()
//            tvWindSpeed.append(tvTemp.context.getString(R.string.wind_speed_metric))
            tvTempMin.text = item.temp.min.roundToInt().toString()
            tvTempMin.append(tvTempMin.context.getString(R.string.celsius_sign))
            tvTempMax.text = item.temp.max.roundToInt().toString()
            tvTempMax.append(tvTempMax.context.getString(R.string.celsius_sign))
            ivIconWeatherDay.load(WeatherConstants.getImageUrl(item.weather[0].icon))
            tvDayOfWeek.text = item.dt.unixSecondsToDayOfWeek()
            tvDate.text = item.dt.unixSecondsToDate()
        }
    }

    override fun getItemCount(): Int = forecastList.size


    class DailyForecastViewHolder(val binding: DailyForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

