package com.bosha.wannaknowweather.ui.selectarea

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bosha.domain.common.WeatherCoordinates
import com.bosha.wannaknowweather.R
import com.bosha.wannaknowweather.databinding.SearchItemBinding
import com.bosha.wannaknowweather.ui.selectarea.SelectAreaViewModel.SearchResultUi
import kotlin.math.roundToInt

class SelectAreaAdapter(private val onClick: (WeatherCoordinates) -> Unit) :
    ListAdapter<SearchResultUi, SelectAreaAdapter.SearchCityViewHolder>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCityViewHolder {
        val binding = SearchItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchCityViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val coords = getItem(adapterPosition).coordinates
                onClick(WeatherCoordinates(coords.lat, coords.lon))
            }
        }
    }

    override fun onBindViewHolder(holder: SearchCityViewHolder, position: Int) {
        holder.binding.apply {
            tvSearchName.text = getItem(position).locationName
            tvCurrentTemp.text = getItem(position).temp.roundToInt().toString()
            tvCurrentTemp.append(tvCurrentTemp.context.getString(R.string.celsius))
        }
    }

    class SearchCityViewHolder(val binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}

private class DiffCallback : DiffUtil.ItemCallback<SearchResultUi>() {

    override fun areItemsTheSame(oldItem: SearchResultUi, newItem: SearchResultUi): Boolean =
        newItem == oldItem


    override fun areContentsTheSame(oldItem: SearchResultUi, newItem: SearchResultUi): Boolean =
        newItem.coordinates == oldItem.coordinates
}
