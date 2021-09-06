package com.bosha.wannaknowweather.ui.currentweather

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    private val viewModel by viewModels<CurrentWeatherViewModel> { factory }

    private var locationPermissionManager: LocationPermissionManager? = null

    override fun onAttach(context: Context) {
        injectDeps()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CurrentWeatherFragmentBinding.inflate(inflater, container, false)
        geCurrentLocation()
        setUpRecycler()
        binding.tbCurrent.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_currentWeatherFragment_to_selectAreaFragment)
        }
        return binding.root
    }

    private fun geCurrentLocation() {
        locationPermissionManager = LocationPermissionManager(requireActivity()).also {
            it.locationPermissionWithSnackBar(binding.root) {
                getLastLocation(::handleLocation)
            }
        }
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

    private fun setUpRecycler() {
        binding.rvHourlyForecast.apply {
            adapter = HourlyForecastAdapter()
            setHasFixedSize(true)
        }
    }

    private fun handleResult(weather: CurrentWeatherViewModel.WeatherUi?) {
        weather ?: return
        binding.apply {
            tvTemp.text = weather.currentWeather.temp.roundToInt().toString()
            (rvHourlyForecast.adapter as HourlyForecastAdapter).forecastList =
                weather.forecast

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
                Toast.makeText(requireContext(), "LOADING", Toast.LENGTH_SHORT).show()
            }
            CurrentWeatherViewModel.SideEffects.ERROR -> {
                Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
            }
            CurrentWeatherViewModel.SideEffects.LOADED -> {
                Toast.makeText(requireContext(), "LOADED", Toast.LENGTH_SHORT).show()
            }
            CurrentWeatherViewModel.SideEffects.EMPTY_LOCATION -> {
                Toast.makeText(requireContext(), "EMPTY_LOCATION", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleLocation(weatherCoordinates: WeatherCoordinates) {
        viewModel.lastKnown = weatherCoordinates
    }

    override fun onDestroyView() {
        _binding = null
        locationPermissionManager?.clear()
        super.onDestroyView()
    }
}