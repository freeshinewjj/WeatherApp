package com.jay.weatherapp.weather.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.jay.weatherapp.R
import com.jay.weatherapp.common.ui.viewmodel.Event
import com.jay.weatherapp.common.util.loadFromUrl
import com.jay.weatherapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.HttpException

@AndroidEntryPoint
class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeFragmentViewModel>()
    private  val locationPermissionRequest: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // approximate location access granted.
                    viewModel.dispatchIntent(HomeFragmentIntent.RequestWeatherForCurrentLocation)
                }
                else -> {
                    // No location access granted.
                    viewModel.dispatchIntent(HomeFragmentIntent.RequestWeatherForLastSearchedCity)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        collectStateUpdate()
        handleLocationPermission()
        setupSearchButton()
    }

    private fun collectStateUpdate() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { homeViewState ->
                    render(homeViewState)
                }
            }
        }
    }

    private fun setupSearchButton() {
        binding.searchButton.setOnClickListener {
            val query = binding.searchBox.text.toString()
            if (query.isNotEmpty()) {
                viewModel.dispatchIntent(HomeFragmentIntent.RequestWeatherWithSearchString(query))
            }
        }
    }

    private fun render(state: HomeFragmentViewState) {
        renderProgress(state.isLoading)
        renderWeatherCard(state.weather)
        handleError(state.failure)
    }

    private fun renderProgress(loading: Boolean) {
        if (loading) {
            binding.progressCircular.show()
        } else {
            binding.progressCircular.hide()
        }
    }

    private fun renderWeatherCard(weather: UIWeather?) {
        if (weather != null) {
            binding.weatherCard.visibility = View.VISIBLE
            binding.cardWeatherTitle.text = weather.cityName
            binding.cardWeatherSubtitle.text = weather.description
            binding.cardWeatherTemp.text = weather.temp.toString() + " F"
            binding.cardWeatherIcon.loadFromUrl(weather.icon)
            binding.cardWeatherTime.text = weather.time
        } else {
            binding.weatherCard.visibility = View.INVISIBLE
        }
    }

    private fun handleError(failure: Event<Throwable>?) {
        val unhandledFailure = failure?.getContentIfNotHandled() ?: return

        var message = unhandledFailure.message
        val fallbackMessage = getString(R.string.general_error)
        var snackbarMessage = if (message.isNullOrEmpty()) {
            fallbackMessage
        } else {
            unhandledFailure.message!!
        }

        val notFoundCode = 404
        if ((unhandledFailure as? HttpException)?.code() == notFoundCode) {
            snackbarMessage = getString(R.string.city_not_found)
        }

        Snackbar.make(requireView(), snackbarMessage, Toast.LENGTH_SHORT).show()
    }

    private fun handleLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.dispatchIntent(HomeFragmentIntent.RequestWeatherForCurrentLocation)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                //show some rationale, ideally we should give option to relaunch
                // grant permission flow
                Toast.makeText(requireContext(), R.string.location_permission_guide, LENGTH_LONG).apply {
                    addCallback(object : Toast.Callback() {
                        override fun onToastHidden() {
                            viewModel.dispatchIntent(HomeFragmentIntent.RequestWeatherForLastSearchedCity)
                        }
                    })
                }.show()
            }
            else -> {
                locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
            }
        }
    }
}