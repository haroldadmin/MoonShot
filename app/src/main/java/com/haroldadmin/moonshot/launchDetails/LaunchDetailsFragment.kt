package com.haroldadmin.moonshot.launchDetails

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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
import com.haroldadmin.moonshot.itemExpandableTextWithHeading
import com.haroldadmin.moonshot.itemLaunchCard
import com.haroldadmin.moonshot.itemLaunchDetail
import com.haroldadmin.moonshot.itemLaunchRocket
import com.haroldadmin.moonshot.itemLoading
import com.haroldadmin.moonshot.itemTextHeader
import com.haroldadmin.moonshot.itemTextWithHeading
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.launch.LaunchStats
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
        epoxyController.setData(state)
    }

    private val epoxyController by lazy {
        asyncTypedEpoxyController(builder, differ, viewModel) { state ->
            when (val launch = state.launch) {
                is Resource.Success -> {
                    buildLaunchModels(this, launch.data)
                }

                is Resource.Error<LaunchMinimal, *> -> {
                    itemError {
                        id("launch-error")
                        error(getString(R.string.launchDetailsFragmentLoading))
                    }

                    if (launch.data != null) {
                        buildLaunchModels(this, launch.data!!)
                    }
                }
                else -> itemLoading {
                    id("launch-loading")
                    message(getString(R.string.launchDetailsFragmentLoadingMessage))
                }
            }
            when (val stats = state.launchStats) {
                is Resource.Success -> {
                    itemTextHeader {
                        id("rocket")
                        header(getString(R.string.launchDetailsFragmentRocketSummaryHeaderText))
                    }
                    itemLaunchRocket {
                        id("rocket-summary")
                        rocketSummary(stats.data.rocket)
                    }
                    itemTextWithHeading {
                        id("first-stage-summary")
                        heading(getString(R.string.launchDetailsFragmentFirstStageSummaryHeader))
                        text("Cores: ${stats.data.firstStageCoreCounts}")
                    }
                    itemTextWithHeading {
                        id("second-stage-summary")
                        heading(getString(R.string.launchDetailsFragmentSecondStageSummaryHeader))
                        text("Payloads: ${stats.data.secondStagePayloadCounts}")
                    }
                }
                is Resource.Error<LaunchStats, *> -> {
                    itemError {
                        id("rocket-summary-error")
                        error(getString(R.string.launchDetailsFragmentRocketSummaryError))
                    }
                    stats.data?.let {
                        itemLaunchRocket {
                            id("rocket-summary")
                            rocketSummary(stats.data!!.rocket)
                        }
                        itemTextWithHeading {
                            id("first-stage-summary")
                            heading(getString(R.string.launchDetailsFragmentFirstStageSummaryHeader))
                            text("Cores: ${stats.data!!.firstStageCoreCounts}")
                        }
                        itemTextWithHeading {
                            id("second-stage-summary")
                            heading(getString(R.string.launchDetailsFragmentSecondStageSummaryHeader))
                            text("Payloads: ${stats.data!!.secondStagePayloadCounts}")
                        }
                    }
                }
                else -> Unit
            }

            when (val pictures = state.launchPictures) {
                is Resource.Success -> {
                    if (pictures.data.images.isNotEmpty()){
                        itemTextHeader {
                            id("photos")
                            header("Photos")
                        }
                        carousel {
                            id("launch-pictures")
                            withModelsFrom(pictures.data.images) { url ->
                                ItemLaunchPictureBindingModel_()
                                    .id(url)
                                    .imageUrl(url)
                            }
                        }
                    }
                }
                else -> Unit
            }
        }
    }

    private fun buildLaunchModels(controller: EpoxyController, launch: LaunchMinimal) {
        with(controller) {
            itemLaunchCard {
                id("header")
                launch(launch)
            }
            itemLaunchDetail {
                id("launch-date")
                detailHeader(getString(R.string.fragmentLaunchDetailsLaunchDateHeader))
                detailName(
                    launch.launchDate?.format(resources.configuration)
                        ?: getString(R.string.launchDetailsFragmentNoLaunchDateText)
                )
                detailIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_round_date_range_24px))
            }
            itemLaunchDetail {
                id("launch-site")
                detailHeader("Launch Site")
                detailName(launch.siteName ?: getString(R.string.fragmentLauchDetailsNoLaunchSiteMessage))
                detailIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_round_place_24px))
                onDetailClick { _ -> showLaunchPadDetails(launch.siteId!!) }
            }
            itemExpandableTextWithHeading {
                id("launch-details")
                heading(getString(R.string.fragmentLaunchDetailsLaunchDetailsHeader))
                text(launch.details ?: getString(R.string.launchDetailsFragmentNoLaunchDetailsText))
            }
        }
    }

    private fun showLaunchPadDetails(siteId: String) {
        LaunchDetailsFragmentDirections.launchPadDetails(siteId).let { action ->
            findNavController().navigate(action)
        }
    }
}