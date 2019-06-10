package com.haroldadmin.moonshot.launchDetails

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.haroldadmin.moonshot.ItemLaunchPictureBindingModel_
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.asyncTypedEpoxyController
import com.haroldadmin.moonshot.base.withModelsFrom
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.databinding.FragmentLaunchDetailsBinding
import com.haroldadmin.moonshot.itemError
import com.haroldadmin.moonshot.itemLaunchCard
import com.haroldadmin.moonshot.itemLaunchRocket
import com.haroldadmin.moonshot.itemLoading
import com.haroldadmin.moonshot.itemTextWithHeading
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.utils.format
import com.haroldadmin.vector.withState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class LaunchDetailsFragment : MoonShotFragment() {

    private lateinit var binding: FragmentLaunchDetailsBinding
    private val safeArgs by navArgs<LaunchDetailsFragmentArgs>()
    private val viewModel by viewModel<LaunchDetailsViewModel> {
        parametersOf(LaunchDetailsState(safeArgs.flightNumber), safeArgs.flightNumber)
    }
    private val differ by inject<Handler>(named("differ"))
    private val builder by inject<Handler>(named("builder"))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLaunchDetailsBinding.inflate(inflater, container, false)
        binding.rvLaunchDetails.setController(epoxyController)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, Observer { renderState() })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        epoxyController.cancelPendingModelBuild()
    }

    override fun renderState() = withState(viewModel) { state ->
        if (epoxyController.currentData != state) {
            epoxyController.setData(state)
        }
    }

    private val epoxyController by lazy {
        asyncTypedEpoxyController(builder, differ, viewModel) { state ->
            when (state.launch) {
                is Resource.Success -> {
                    buildLaunchModels(this, state.launch.data)
                }

                is Resource.Error<Launch, *> -> {
                    itemError {
                        id("launch-error")
                        error(getString(R.string.launchDetailsFragmentLoading))
                    }

                    if (state.launch.data != null) {
                        buildLaunchModels(this, state.launch.data!!)
                    }
                }
                else -> itemLoading {
                    id("launch-loading")
                    message(getString(R.string.launchDetailsFragmentLoadingMessage))
                }
            }
            when (state.rocketSummary) {
                is Resource.Success -> itemLaunchRocket {
                    id("rocket-summary")
                    rocketSummary(state.rocketSummary.data)
                }
                is Resource.Error<RocketSummary, *> -> itemError {
                    id("rocket-summary-error")
                    error(getString(R.string.launchDetailsFragmentRocketSummaryError))
                }
                else -> itemLoading {
                    id("rocket-summary-loading")
                    message(getString(R.string.launchDetailsFragmentRocketSummaryLoading))
                }
            }
        }
    }

    private fun buildLaunchModels(controller: EpoxyController, launch: Launch) {
        with(controller) {
            itemLaunchCard {
                id("header-${launch.flightNumber}")
                launch(launch)
            }
            itemTextWithHeading {
                id("launch-date-${launch.flightNumber}")
                heading("Launch Date")
                text(launch.launchDate.format(resources.configuration))
            }
            itemTextWithHeading {
                id("launch-details-${launch.flightNumber}")
                heading("Details")
                text(launch.details)
            }

            launch.links?.flickrImages?.let { imageUrls ->
                carousel {
                    id("launch-pictures")
                    withModelsFrom(imageUrls) { url ->
                        ItemLaunchPictureBindingModel_()
                            .id(url)
                            .imageUrl(url)
                    }
                }
            }
        }
    }
}