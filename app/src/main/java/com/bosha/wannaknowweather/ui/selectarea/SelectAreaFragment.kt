package com.bosha.wannaknowweather.ui.selectarea

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bosha.wannaknowweather.databinding.SelectAreaFragmentBinding
import com.bosha.wannaknowweather.ui.ViewModelFactory
import com.bosha.wannaknowweather.utils.injectDeps
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SelectAreaFragment : Fragment() {


    private var _binding: SelectAreaFragmentBinding? = null
    private val binding get() = checkNotNull(_binding)

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<SelectAreaViewModel> { factory }

    override fun onAttach(context: Context) {
        injectDeps()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SelectAreaFragmentBinding.inflate(inflater, container, false).apply {
            setUpRecycler(this)
        }
        return binding.root
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.searchResult
                .onEach(::handleResult)
                .launchIn(this)


            callbackFlow {
                binding.etSearch.doAfterTextChanged {
                    trySend(it.toString())
                }
                awaitClose()
            }
                .debounce(400)
                .distinctUntilChanged()
                .onEach { viewModel.findCity(it) }
                .launchIn(this)
        }
    }

    private fun setUpRecycler(binding: SelectAreaFragmentBinding) {
        binding.rvCitySelect.apply {
            adapter = SelectAreaAdapter {}
            setHasFixedSize(true)
        }
    }

    private fun handleResult(result: List<SelectAreaViewModel.SearchResult>) {
        (binding.rvCitySelect.adapter as SelectAreaAdapter).submitList(result)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}