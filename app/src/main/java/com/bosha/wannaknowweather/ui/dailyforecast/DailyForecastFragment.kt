package com.bosha.wannaknowweather.ui.dailyforecast

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bosha.domain.entities.DailyForecast
import com.bosha.wannaknowweather.R
import com.bosha.wannaknowweather.databinding.DailyForecastFragmentBinding
import com.bosha.wannaknowweather.ui.ViewModelFactory
import com.bosha.wannaknowweather.ui.currentweather.CurrentWeatherViewModel
import com.bosha.wannaknowweather.utils.injectDeps
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DailyForecastFragment : Fragment() {

    private var _binding: DailyForecastFragmentBinding? = null
    private val binding get() = checkNotNull(_binding)

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<DailyForecastViewModel> { factory }

    override fun onAttach(context: Context) {
        injectDeps()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DailyForecastFragmentBinding.inflate(inflater, container, false)
        setUpRecycler()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.forecast
                .onEach(::handleResult)
                .launchIn(this)
        }
        binding.tbDaystoolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.pbLoading.show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun handleResult(forecast: List<DailyForecast>?){
        forecast ?: return
        (binding.rvDaily.adapter as DailyForecastAdapter).forecastList = forecast
        binding.pbLoading.hide()
    }

    private fun setUpRecycler() {
        binding.apply {
            rvDaily.adapter = DailyForecastAdapter()
            rvDaily.setHasFixedSize(true)
        }
    }
}

