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
import com.haroldadmin.moonshot.di.appComponent
import com.haroldadmin.moonshot.launchDetails.databinding.FragmentLaunchDetailsBinding
import com.haroldadmin.moonshot.launchDetails.di.DaggerLaunchDetailsComponent
import com.haroldadmin.moonshot.launchDetails.views.LinkCardModel_
import com.haroldadmin.moonshot.launchDetails.views.PictureCardModel_
import com.haroldadmin.moonshot.launchDetails.views.YouTubeCardModel_
import com.haroldadmin.moonshot.launchDetails.views.rocketSummaryCard
import com.haroldadmin.moonshot.models.DatePrecision
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.relevantLinks
import com.haroldadmin.moonshot.utils.formatDate
import com.haroldadmin.moonshot.views.errorView
import com.haroldadmin.moonshot.views.expandableTextView
import com.haroldadmin.moonshot.views.launchCard
import com.haroldadmin.moonshot.views.detailCard
import com.haroldadmin.moonshot.views.loadingView
import com.haroldadmin.moonshot.views.sectionHeaderView
import com.haroldadmin.vector.activityViewModel
import com.haroldadmin.vector.fragmentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Date
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LaunchDetailsFragment : ComplexMoonShotFragment<LaunchDetailsViewModel, LaunchDetailsState>() {

    private lateinit var binding: FragmentLaunchDetailsBinding

    @Inject lateinit var viewModelFactory: LaunchDetailsViewModel.Factory
    @Inject lateinit var mainViewModelFactory: MainViewModel.Factory

    override val viewModel: LaunchDetailsViewModel by fragmentViewModel { initState, _ ->
        viewModelFactory.create(initState)
    }

    private val mainViewModel: MainViewModel by activityViewModel { initState, savedStateHandle ->
        mainViewModelFactory.create(initState, savedStateHandle)
    }

    override fun initDI() {
        DaggerLaunchDetailsComponent.builder()
            .appComponent(appComponent())
            .build()
            .inject(this)
    }

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

            is Resource.Error<Launch, *> -> {
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

        when (val pictures = state.launchPictures) {
            is Resource.Success -> {
                pictures().flickrImages
                    ?.filter { it.isNotBlank() }
                    ?.takeIf { it.isNotEmpty() }
                    ?.let { pics ->
                        sectionHeaderView {
                            id("photos")
                            header("Photos")
                        }
                        carousel {
                            id("launch-pictures")
                            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                            withModelsFrom(pics) { url ->
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

    private fun launchModels(controller: EpoxyController, launch: Launch) {
        with(controller) {
            launchCard {
                id("header")
                launch(launch)
            }

            detailCard {
                id("launch-date")
                header(getString(R.string.fragmentLaunchDetailsLaunchDateHeader))
                content(formatDate(launch.launchDateUtc, launch.tentativeMaxPrecision))
                icon(appR.drawable.ic_round_date_range_24px)
            }

            detailCard {
                id("launch-site")
                header(getString(R.string.fragmentLaunchDetailsLaunchSiteHeader))
                content(launch.launchSite?.siteName ?: getString(R.string.fragmentLaunchDetailsNoLaunchSiteMessage))
                icon(appR.drawable.ic_round_place_24px)
                onDetailClick { _ -> launch.launchSite?.let { showLaunchPadDetails(it.siteId) } }
            }

            detailCard {
                id("launch-success")
                header(getString(R.string.fragmentLaunchDetailsLaunchStatusHeader))
                content(
                    when {
                        launch.isUpcoming == true -> getString(R.string.launchStatusUpcoming)
                        launch.launchSuccess == true -> getString(R.string.launchStatusSuccessful)
                        launch.launchSuccess == false -> getString(R.string.launchStatusUnsuccessful)
                        else -> getString(R.string.launchStatusUnknown)
                    }
                )
                icon(R.drawable.ic_round_flight_takeoff_24px)
            }

            expandableTextView {
                id("launch-details")
                header(getString(R.string.fragmentLaunchDetailsLaunchDetailsHeader))
                content(launch.details ?: getString(R.string.fragmentLaunchDetailsNoLaunchDetailsMessage))
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }

            rocketSummaryCard {
                id("launch-rocket-summary")
                rocket(launch.rocket)
                onRocketClick { _ ->
                    val action = LaunchDetailsFragmentDirections.launchRocketDetails(launch.rocket.rocketId)
                    findNavController().navigate(action)
                }
            }

            launch.relevantLinks()
                .takeIf { it.isNotEmpty() }
                ?.let { map -> buildLinks(map, this) }
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

    private fun formatDate(date: Date, precision: DatePrecision): String {
        return requireContext().formatDate(date, precision.dateFormat)
    }
}