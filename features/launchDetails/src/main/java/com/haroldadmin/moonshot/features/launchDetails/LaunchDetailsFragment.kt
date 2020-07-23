package com.haroldadmin.moonshot.features.launchDetails

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
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.base.ComplexMoonShotFragment
import com.haroldadmin.moonshot.base.asyncController
import com.haroldadmin.moonshot.base.layoutAnimation
import com.haroldadmin.moonshot.base.withModelsFromIndexed
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.di.appComponent
import com.haroldadmin.moonshot.features.launchDetails.di.DaggerLaunchDetailsComponent
import com.haroldadmin.moonshot.features.launchDetails.views.PictureCardModel_
import com.haroldadmin.moonshot.features.launchDetails.views.missionSummaryCard
import com.haroldadmin.moonshot.features.launchDetails.views.rocketSummaryCard
import com.haroldadmin.moonshot.features.launchDetails.databinding.FragmentLaunchDetailsBinding
import com.haroldadmin.moonshot.models.DatePrecision
import com.haroldadmin.moonshot.models.LinkPreview
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.missionPatch
import com.haroldadmin.moonshot.utils.formatDate
import com.haroldadmin.moonshot.views.LinkPreviewCardModel_
import com.haroldadmin.moonshot.views.detailCard
import com.haroldadmin.moonshot.views.errorView
import com.haroldadmin.moonshot.views.expandableTextView
import com.haroldadmin.moonshot.views.launchCard
import com.haroldadmin.moonshot.views.loadingView
import com.haroldadmin.moonshot.views.sectionHeaderView
import com.haroldadmin.vector.activityViewModel
import com.haroldadmin.vector.fragmentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Date
import javax.inject.Inject
import com.haroldadmin.moonshot.R as appR

@ExperimentalCoroutinesApi
class LaunchDetailsFragment : ComplexMoonShotFragment<LaunchDetailsViewModel, LaunchDetailsState>() {

    private var _binding: FragmentLaunchDetailsBinding? = null
    private val binding: FragmentLaunchDetailsBinding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: LaunchDetailsViewModel.Factory
    @Inject
    lateinit var mainViewModelFactory: MainViewModel.Factory

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
        _binding = FragmentLaunchDetailsBinding.inflate(inflater, container, false)

        mainViewModel.setTitle(getString(appR.string.title_launch_details))

        binding.rvLaunchDetails.apply {
            setController(epoxyController)
            layoutAnimation = layoutAnimation(appR.anim.layout_animation_fade_in)
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun epoxyController() = asyncController(viewModel) { state ->
        when (val launch = state.launch) {
            is Resource.Success -> launchModels(launch())

            is Resource.Error<Launch, *> -> {
                errorView {
                    id("launch-error")
                    errorText(getString(R.string.fragmentLaunchDetailsErrorMessage))
                }

                launch()?.let { data ->
                    launchModels(data)
                }
            }

            is Resource.Loading -> loadingView {
                id("launch-loading")
                loadingText(getString(R.string.fragmentLaunchDetailsLoadingMessage))
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }

            else -> Unit
        }

        if (state.linkPreviews.isNotEmpty()) {
            buildLinkModels(state.linkPreviews)
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
                            withModelsFromIndexed(pics) { index, url ->
                                PictureCardModel_()
                                    .id(url)
                                    .imageUrl(url)
                                    .onPhotoClick { _ ->
                                        val urls = pics.toTypedArray()
                                        findNavController().navigate(
                                            LaunchDetailsFragmentDirections.showPhoto(
                                                urls,
                                                index
                                            )
                                        )
                                    }
                            }
                        }
                    }
            }
            else -> Unit
        }
    }

    private fun EpoxyController.launchModels(launch: Launch) {
        launchCard {
            id("header")
            launch(launch)
            onMissionPatchClick { _ ->
                launch.missionPatch()?.let { url ->
                    findNavController().navigate(
                        LaunchDetailsFragmentDirections.showPhoto(
                            arrayOf(url),
                            0
                        )
                    )
                }
            }
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
            onDetailClick { _ -> launch.launchSite?.siteId?.let { showLaunchPadDetails(it) } }
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
                val action =
                    LaunchDetailsFragmentDirections.launchRocketDetails(
                        launch.rocket.rocketId
                    )
                findNavController().navigate(action)
            }
        }

        launch.missionId
            .filter { it.isNotBlank() }
            .forEach { id ->
                missionSummaryCard {
                    id(id)
                    missionId(id)
                    onMissionClick { missionId ->
                        val action =
                            LaunchDetailsFragmentDirections.missionDetails(
                                missionId
                            )
                        findNavController().navigate(action)
                    }
                }
            }
    }

    private fun EpoxyController.buildLinkModels(linkPreviews: List<LinkPreview>) {

        sectionHeaderView {
            id("links-header")
            header(getString(R.string.fragmentLaunchDetailsLinksHeader))
        }

        carousel {
            id("links-carousel")
            withModelsFromIndexed(linkPreviews) { index, linkPreview ->
                LinkPreviewCardModel_()
                    .id(index)
                    .linkPreview(linkPreview)
                    .onLinkClick { linkPrev ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkPrev.url))
                        startActivity(intent)
                    }
            }
        }
    }

    private fun showLaunchPadDetails(siteId: String) {
        LaunchDetailsFragmentDirections.launchPadDetails(
            siteId
        )
            .let { action -> findNavController().navigate(action) }
    }

    private fun formatDate(date: Date, precision: DatePrecision): String {
        return requireContext().formatDate(date, precision.dateFormat)
    }
}