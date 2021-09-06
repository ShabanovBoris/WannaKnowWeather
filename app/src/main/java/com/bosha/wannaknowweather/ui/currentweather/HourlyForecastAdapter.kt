package com.bosha.wannaknowweather.ui.currentweather

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bosha.data.api.WeatherConstants
import com.bosha.domain.entities.HourlyForecast
import com.bosha.wannaknowweather.R
import com.bosha.wannaknowweather.databinding.HourlyForecastItemBinding
import com.bosha.wannaknowweather.utils.unixSecondsToTime
import okhttp3.HttpUrl.Companion.toHttpUrl
import kotlin.math.roundToInt

class HourlyForecastAdapter :
    RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder>() {

    var forecastList: List<HourlyForecast> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder =
        HourlyForecastViewHolder(
            HourlyForecastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        holder.binding.apply {
            tvTime.text = forecastList[position].dt.unixSecondsToTime()
            tvTemp.text =  forecastList[position].temp.roundToInt().toString()
            tvTemp.append(tvTemp.context.getString(R.string.celsius_sign))
            ivIconWeather.load(WeatherConstants.getImageUrl(forecastList[position].weather[0].icon))
            tvWindSpeed.text =  forecastList[position].windSpeed.toString()
            tvWindSpeed.append(tvTemp.context.getString(R.string.wind_speed_metric))
        }
    }

    override fun getItemCount(): Int = forecastList.size


    class HourlyForecastViewHolder(val binding: HourlyForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

