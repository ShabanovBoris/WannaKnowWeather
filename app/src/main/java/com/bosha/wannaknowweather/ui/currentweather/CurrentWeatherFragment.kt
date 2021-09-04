package com.bosha.wannaknowweather.ui.currentweather

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bosha.domain.common.WeatherCoordinates
import com.bosha.wannaknowweather.databinding.CurrentWeatherFragmentBinding
import com.bosha.wannaknowweather.ui.ViewModelFactory
import com.bosha.wannaknowweather.utils.LocationPermisson.LocationPermissionManager
import com.bosha.wannaknowweather.utils.LocationPermisson.getLastLocation
import com.bosha.wannaknowweather.utils.injectDeps
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


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
        locationPermissionManager = LocationPermissionManager(requireActivity()).also {
            it.locationPermissionWithSnackBar(binding.root) {
                getLastLocation(::handleLocation)
            }
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.weatherResultFlow
                .onEach(::handleResult)
                .launchIn(this)

            viewModel.sideEffect
                .onEach(::handleSideEffect)
                .launchIn(this)
        }
    }

    private fun handleResult(weather: CurrentWeatherViewModel.CurrentWeatherUi?) {
        binding.tvTemp.text = weather?.weather?.temp.toString()
        if (weather?.locality.isNullOrEmpty()) {
            binding.tvCurrentCity.text = weather?.areaName
        } else {
            binding.tvCurrentCity.text = weather?.locality
        }
    }

    private fun handleSideEffect(sideEffect: CurrentWeatherViewModel.SideEffectActions) {
        when (sideEffect) {
            CurrentWeatherViewModel.SideEffectActions.LOADING -> {

            }
            CurrentWeatherViewModel.SideEffectActions.ERROR -> {

            }
            CurrentWeatherViewModel.SideEffectActions.LOADED -> {

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