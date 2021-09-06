package com.bosha.wannaknowweather.ui.selectarea

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bosha.wannaknowweather.ui.selectarea.SelectAreaViewModel.SearchResult
import com.bosha.wannaknowweather.databinding.SearchItemBinding
import kotlin.math.roundToInt

class SelectAreaAdapter(private val onClick: () -> Unit) : ListAdapter<SearchResult, SelectAreaAdapter.SearchCityViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCityViewHolder {
        val binding = SearchItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
            .apply {
                root.setOnClickListener {
                    onClick()
                }
            }

        return SearchCityViewHolder(binding)
    }


    //TODO refactor
    override fun onBindViewHolder(holder: SearchCityViewHolder, position: Int) {
        holder.binding.apply {
            tvSearchName.text = getItem(position).locationName
            tvCurrentTemp.text = getItem(position).temp.roundToInt().toString()
        }
    }

    class SearchCityViewHolder(val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root)

}

private class DiffCallback : DiffUtil.ItemCallback<SearchResult>() {

    override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean =
        newItem == oldItem


    override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean =
        newItem.coordinates  == oldItem.coordinates
}