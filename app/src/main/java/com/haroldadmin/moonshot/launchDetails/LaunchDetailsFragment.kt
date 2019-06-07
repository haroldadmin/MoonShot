package com.haroldadmin.moonshot.launchDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.typedEpoxyController
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.databinding.FragmentLaunchDetailsBinding
import com.haroldadmin.moonshot.itemError
import com.haroldadmin.moonshot.itemLaunchHeader
import com.haroldadmin.moonshot.itemLoading
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.vector.withState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LaunchDetailsFragment : MoonShotFragment() {

    private lateinit var binding: FragmentLaunchDetailsBinding
    private val safeArgs by navArgs<LaunchDetailsFragmentArgs>()
    private val viewModel by viewModel<LaunchDetailsViewModel> {
        parametersOf(LaunchDetailsState(safeArgs.flightNumber), safeArgs.flightNumber)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLaunchDetailsBinding.inflate(inflater, container, false)
        binding.rvLaunchDetails.setController(epoxyController)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, Observer { renderState() })
    }

    override fun renderState() = withState(viewModel) { state ->
        epoxyController.setData(state)
    }

    private val epoxyController by lazy {
        typedEpoxyController(viewModel) { state ->
            when (state.launch) {
                is Resource.Success -> {
                    itemLaunchHeader {
                        id("header-${state.launch.data.flightNumber}")
                        launch(state.launch.data)
                    }
                }
                is Resource.Error<Launch, *> -> itemError {
                    id("launch-error")
                    error("Unable to load launch details")
                }
                else -> itemLoading {
                    id("launch-loading")
                    message("Loading launch details")
                }
            }
        }
    }
}