package com.bosha.wannaknowweather.ui.currentweather

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bosha.domain.common.WeatherCoordinates
import com.bosha.wannaknowweather.R
import com.bosha.wannaknowweather.databinding.CurrentWeatherFragmentBinding
import com.bosha.wannaknowweather.ui.ViewModelFactory
import com.bosha.wannaknowweather.utils.injectDeps
import com.bosha.wannaknowweather.utils.location.LocationPermissionManager
import com.bosha.wannaknowweather.utils.location.getLastLocation
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.math.roundToInt

class CurrentWeatherFragment : Fragment() {

    private var _binding: CurrentWeatherFragmentBinding? = null
    private val binding get() = checkNotNull(_binding)

    @Inject
    lateinit var factory: ViewModelFactory
    @Inject
    lateinit var locationPermissionManager: LocationPermissionManager

    private val viewModel by viewModels<CurrentWeatherViewModel> { factory }

    override fun onAttach(context: Context) {
        injectDeps()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CurrentWeatherFragmentBinding.inflate(inflater, container, false)
        setUpRecycler()
        setUpButtons()
        firstLocationPermission()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.weather
                .onEach(::handleResult)
                .launchIn(this)

            viewModel.sideEffect
                .onEach(::handleSideEffect)
                .launchIn(this)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        locationPermissionManager.clear()
        super.onDestroy()
    }

    private fun setUpRecycler() {
        binding.rvHourlyForecast.apply {
            adapter = HourlyForecastAdapter()
            setHasFixedSize(true)
        }
    }

    private fun setUpButtons() {
        binding.tbCurrent.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_currentWeatherFragment_to_selectAreaFragment)
        }
        binding.tbCurrent.setOnMenuItemClickListener {
            if (it.itemId == R.id.b_location) {
                locationPermissionManager.locationPermission { granted ->
                    if (granted) requireContext().getLastLocation(::handleLocation)
                }
            }
            return@setOnMenuItemClickListener true
        }
        binding.bTo7dayForecast.setOnClickListener {
            findNavController().navigate(R.id.action_currentWeatherFragment_to_dailyForecastFragment)
        }
    }

    private fun firstLocationPermission() {
        locationPermissionManager.locationPermissionWithSnackBar(true) {
            getLastLocation(::handleLocation)
        }
    }

    private fun handleResult(weather: CurrentWeatherViewModel.WeatherUi?) {
        weather ?: return
        binding.apply {
            tvTemp.text = weather.currentWeather.temp.roundToInt().toString()

            tvName.text = weather.currentWeather.weather[0].main

            (rvHourlyForecast.adapter as HourlyForecastAdapter).forecastList =
                weather.forecast

            tvCelsiusSign.isVisible = true

            // in example [locality] is Moscow, [areaName] is Moscow oblast'
            if (weather.locality.isEmpty()) {
                tvCurrentCity.text = weather.areaName
            } else {
                tvCurrentCity.text = weather.locality
            }
        }
    }

    private fun handleSideEffect(sideEffect: CurrentWeatherViewModel.SideEffects) {
        when (sideEffect) {
            CurrentWeatherViewModel.SideEffects.LOADING -> {
                binding.pbLoading.show()
                binding.bTo7dayForecast.isVisible = false
            }
            CurrentWeatherViewModel.SideEffects.ERROR -> {
                binding.pbLoading.hide()
                Toast.makeText(requireContext(), "Error loading data", Toast.LENGTH_LONG).show()
            }
            CurrentWeatherViewModel.SideEffects.LOADED -> {
                binding.pbLoading.hide()
                binding.tvHint.isVisible = false
                binding.bTo7dayForecast.isVisible = true
            }
            CurrentWeatherViewModel.SideEffects.EMPTY_LOCATION -> {
                binding.pbLoading.hide()
                binding.tvHint.isVisible = true
            }
        }
    }

    private fun handleLocation(weatherCoordinates: WeatherCoordinates) {
        viewModel.lastKnownLocation = weatherCoordinates
    }
}