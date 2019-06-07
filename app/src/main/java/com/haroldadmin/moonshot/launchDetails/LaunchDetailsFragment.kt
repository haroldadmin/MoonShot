package com.haroldadmin.moonshot.launchDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.typedEpoxyController
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.databinding.FragmentLaunchDetailsBinding
import com.haroldadmin.moonshot.itemError
import com.haroldadmin.moonshot.itemLaunchHeader
import com.haroldadmin.moonshot.itemLaunchRocket
import com.haroldadmin.moonshot.itemLoading
import com.haroldadmin.moonshot.itemTextWithHeading
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.utils.format
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
                        backdrop(state.launch.data.backdropImageUrl)
                        missionPatch(state.launch.data.missionPatch)
                        missionName(state.launch.data.missionName)
                    }
                    itemTextWithHeading {
                        id("launch-date-${state.launch.data.flightNumber}")
                        heading("Launch Date")
                        text(state.launch.data.launchDate.format(resources.configuration))
                    }
                    itemTextWithHeading {
                        id("launch-details-${state.launch.data.flightNumber}")
                        heading("Details")
                        text(state.launch.data.details)
                    }
                }
                is Resource.Error<Launch, *> -> itemError {
                    id("launch-error")
                    error("Unable to load launch details")
                    return@typedEpoxyController // We don't want to load other details if the launch details are not present
                }
                else -> itemLoading {
                    id("launch-loading")
                    message("Loading launch details")
                    return@typedEpoxyController // We don't want to load other details if the launch details are loading
                }
            }
            when (state.rocketSummary) {
                is Resource.Success -> itemLaunchRocket {
                    id("rocket-summary")
                    rocketSummary(state.rocketSummary.data)
                }
                is Resource.Error<RocketSummary, *> -> itemError {
                    id("rocket-summary-error")
                    error("Unable to load rocket summary")
                }
                else -> itemLoading {
                    id("rocket-summary-loading")
                    message("Loading rocket summary")
                }
            }
        }
    }
}