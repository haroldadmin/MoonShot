package com.haroldadmin.moonshot.launchDetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.haroldadmin.moonshot.R as appR
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.base.ComplexMoonShotFragment
import com.haroldadmin.moonshot.base.asyncController
import com.haroldadmin.moonshot.base.layoutAnimation
import com.haroldadmin.moonshot.base.withModelsFrom
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.launchDetails.databinding.FragmentLaunchDetailsBinding
import com.haroldadmin.moonshot.launchDetails.views.LinkCardModel_
import com.haroldadmin.moonshot.launchDetails.views.PictureCardModel_
import com.haroldadmin.moonshot.launchDetails.views.YouTubeCardModel_
import com.haroldadmin.moonshot.launchDetails.views.rocketSummaryCard
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.launch.LaunchStats
import com.haroldadmin.moonshot.views.errorView
import com.haroldadmin.moonshot.views.expandableTextView
import com.haroldadmin.moonshot.views.launchCard
import com.haroldadmin.moonshot.views.detailCard
import com.haroldadmin.moonshot.views.loadingView
import com.haroldadmin.moonshot.views.sectionHeaderView
import com.haroldadmin.moonshot.views.textCard
import com.haroldadmin.vector.activityViewModel
import com.haroldadmin.vector.fragmentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class LaunchDetailsFragment : ComplexMoonShotFragment<LaunchDetailsViewModel, LaunchDetailsState>() {

    private lateinit var binding: FragmentLaunchDetailsBinding

    override val viewModel: LaunchDetailsViewModel by fragmentViewModel()
    private val mainViewModel: MainViewModel by activityViewModel()

    override fun initDI() = LaunchDetails.init()

    override fun renderer(state: LaunchDetailsState) {
        epoxyController.setData(state)
        state.launch()?.let { launch ->
            mainViewModel.setTitle(launch.missionName)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLaunchDetailsBinding.inflate(inflater, container, false)

        mainViewModel.setTitle(getString(appR.string.title_launch_details))

        binding.rvLaunchDetails.apply {
            setController(epoxyController)
            layoutAnimation = layoutAnimation(appR.anim.layout_animation_fade_in)
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        return binding.root
    }

    override fun epoxyController() = asyncController(viewModel) { state ->

        when (val launch = state.launch) {
            is Resource.Success -> launchModels(this, launch())

            is Resource.Error<LaunchMinimal, *> -> {
                errorView {
                    id("launch-error")
                    errorText(getString(R.string.fragmentLaunchDetailsErrorMessage))
                }

                launch()?.let { data ->
                    launchModels(this, data)
                }
            }
            else -> loadingView {
                id("launch-loading")
                loadingText(getString(R.string.fragmentLaunchDetailsLoadingMessage))
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }
        }

        when (val stats = state.launchStats) {
            is Resource.Success -> {
                sectionHeaderView {
                    id("rocket")
                    header(getString(R.string.fragmentLaunchDetailsRocketSummaryHeader))
                }
                launchStatsModels(stats(), this)
            }
            is Resource.Error<LaunchStats, *> -> {
                sectionHeaderView {
                    id("rocket")
                    header(getString(R.string.fragmentLaunchDetailsRocketSummaryHeader))
                }
                errorView {
                    id("rocket-summary-error")
                    errorText(getString(R.string.fragmentLaunchDetailsRocketSummaryError))
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
                stats()?.let { data ->
                    launchStatsModels(data, this)
                }
            }
            else -> Unit
        }

        when (val pictures = state.launchPictures) {
            is Resource.Success -> {
                if (pictures().images.isNotEmpty()) {
                    sectionHeaderView {
                        id("photos")
                        header("Photos")
                    }
                    carousel {
                        id("launch-pictures")
                        spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                        withModelsFrom(pictures.data.images) { url ->
                            PictureCardModel_()
                                .id(url)
                                .imageUrl(url)
                        }
                    }
                }
            }
            else -> Unit
        }
    }

    private fun launchModels(controller: EpoxyController, launch: LaunchMinimal) {
        with(controller) {
            launchCard {
                id("header")
                launch(launch)
            }

            detailCard {
                id("launch-date")
                header(getString(R.string.fragmentLaunchDetailsLaunchDateHeader))
                content(launch.launchDateText)
                icon(appR.drawable.ic_round_date_range_24px)
            }

            detailCard {
                id("launch-site")
                header(getString(R.string.fragmentLaunchDetailsLaunchSiteHeader))
                content(launch.siteName ?: getString(R.string.fragmentLaunchDetailsNoLaunchSiteMessage))
                icon(appR.drawable.ic_round_place_24px)
                onDetailClick { _ -> showLaunchPadDetails(launch.siteId!!) }
            }

            detailCard {
                id("launch-success")
                header(getString(R.string.fragmentLaunchDetailsLaunchStatusHeader))
                content({
                    when {
                        launch.launchSuccess == true -> "Successful"
                        launch.launchSuccess == false -> "Unsuccessful"
                        else -> "Unknown"
                    }
                }.invoke())
                icon(R.drawable.ic_round_flight_takeoff_24px)
            }

            expandableTextView {
                id("launch-details")
                header(getString(R.string.fragmentLaunchDetailsLaunchDetailsHeader))
                content(launch.details ?: getString(R.string.fragmentLaunchDetailsNoLaunchDetailsMessage))
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }
            launch
                .links
                .filterValues { !it.isNullOrBlank() }
                .takeIf { it.isNotEmpty() }
                ?.let { map ->
                    @Suppress("UNCHECKED_CAST")
                    buildLinks(map as Map<String, String>, this)
                }
        }
    }

    private fun launchStatsModels(stats: LaunchStats, controller: EpoxyController) = with(controller) {

        if (stats.rocket == null) {
            return@with
        }

        rocketSummaryCard {
            id("rocket-summary")
            rocket(stats.rocket!!)
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            onRocketClick { _ ->
                val action = LaunchDetailsFragmentDirections.launchRocketDetails(stats.rocket!!.rocketId)
                findNavController().navigate(action)
            }
        }
        textCard {
            id("first-stage-summary")
            header(getString(R.string.fragmentLaunchDetailsFirstStageSummaryHeader))
            content("Cores: ${stats.firstStageCoreCounts}")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount / 2 }
        }
        textCard {
            id("second-stage-summary")
            header(getString(R.string.fragmentLaunchDetailsSecondStageSummaryHeader))
            content("Payloads: ${stats.secondStagePayloadCounts}")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount / 2 }
        }
    }

    private fun buildLinks(links: Map<String, String>, controller: EpoxyController) = with(controller) {
        sectionHeaderView {
            id("links")
            header(getString(R.string.fragmentLaunchDetailsLinksHeader))
        }

        carousel {
            id("launch-links")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            withModelsFrom(links) { name, link ->
                when {
                    name.contains("YouTube") -> {
                        YouTubeCardModel_()
                            .id(link)
                            .thumbnailUrl(link.youtubeThumbnail())
                            .onYoutubeClick { _ ->
                                Intent(Intent.ACTION_VIEW, Uri.parse(link.youtubeVideo()))
                                    .also { startActivity(it) }
                            }
                    }
                    name.contains("Reddit") -> {
                        LinkCardModel_()
                            .id(link)
                            .title(name)
                            .gradient(R.drawable.gradient_reddit)
                            .onLinkClick { _ ->
                                Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                    .also { startActivity(it) }
                            }
                    }
                    else -> {
                        LinkCardModel_()
                            .id(link)
                            .title(name)
                            .gradient(R.drawable.gradient_wikipedia)
                            .onLinkClick { _ ->
                                Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                    .also { startActivity(it) }
                            }
                    }
                }
            }
        }
    }

    private fun showLaunchPadDetails(siteId: String) {
        LaunchDetailsFragmentDirections.launchPadDetails(siteId)
            .let { action -> findNavController().navigate(action) }
    }
}