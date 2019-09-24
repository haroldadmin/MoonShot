package com.haroldadmin.moonshot.rocketDetails

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.airbnb.epoxy.carousel
import com.haroldadmin.moonshot.ItemLaunchCardBindingModel_
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.asyncTypedEpoxyController
import com.haroldadmin.moonshot.base.withModelsFrom
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.databinding.FragmentRocketDetailsBinding
import com.haroldadmin.moonshot.itemLaunchDetail
import com.haroldadmin.moonshot.itemRocket
import com.haroldadmin.moonshot.itemTextHeader
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.moonshot.utils.format
import com.haroldadmin.moonshot.views.errorView
import com.haroldadmin.moonshot.views.expandableTextView
import com.haroldadmin.moonshot.views.loadingView
import com.haroldadmin.vector.activityViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import com.haroldadmin.moonshot.R as appR

class RocketDetailsFragment : MoonShotFragment() {

    private lateinit var binding: FragmentRocketDetailsBinding
    private val safeArgs by navArgs<RocketDetailsFragmentArgs>()
    private val builder by inject<Handler>(named("builder"))
    private val differ by inject<Handler>(named("differ"))
    private val viewModel by viewModel<RocketDetailsViewModel> {
        val initialState =
            RocketDetailsState(rocketId = safeArgs.rocketId)
        parametersOf(initialState)
    }
    private val mainViewModel: MainViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RocketDetails.init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRocketDetailsBinding.inflate(inflater, container, false)
        mainViewModel.setTitle(getString(appR.string.title_rocket_details))
        val animation =
            AnimationUtils.loadLayoutAnimation(requireContext(), appR.anim.layout_animation_fade_in)
        binding.rvRocketDetails.apply {
            setController(epoxyController)
            layoutAnimation = animation
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        renderState(viewModel) { state ->
            epoxyController.setData(state)
            state.rocket()?.let { mainViewModel.setTitle(it.rocketName) }
        }
    }

    private val epoxyController by lazy {
        asyncTypedEpoxyController(builder, differ, viewModel) { state ->
            when (val rocket = state.rocket) {
                is Resource.Success -> {
                    itemRocket {
                        id(rocket.data.rocketId)
                        this.rocket(rocket.data)
                    }
                    expandableTextView {
                        id("description")
                        header(getString(R.string.rocketDetailsFragmentRocketDescriptionHeader))
                        content(rocket.data.description)
                    }
                    itemLaunchDetail {
                        id("cost-per-launch")
                        detailHeader(getString(R.string.rocketDetailsFragmentCostPerLaunchHeader))
                        detailName("$ ${rocket.data.costPerLaunch.format(resources.configuration)}")
                    }
                    itemLaunchDetail {
                        id("success-percentage")
                        detailHeader(getString(R.string.rocketDetailsFragmentSuccessPercentageHeader))
                        detailName(rocket.data.successRatePercentage.toString())
                    }
                }
                is Resource.Error<RocketMinimal, *> -> {
                    errorView {
                        id("rocket-details-error")
                        errorText(getString(R.string.rocketDetailsFragmentRocketDetailsErrorMessage))
                    }
                }
                else -> loadingView {
                    id("rocket-details-loading")
                    loadingText(getString(R.string.rocketDetailsFragmentRocketLoadingMessage))
                }
            }

            when (val launches = state.launches) {
                is Resource.Success -> {
                    if (launches.data.isEmpty()) return@asyncTypedEpoxyController

                    itemTextHeader {
                        id("launches-header")
                        header(getString(R.string.rocketDetailsFragmentLaunchesHeader))
                    }

                    carousel {
                        id("rocket-launches")
                        withModelsFrom(launches.data) { launch ->
                            ItemLaunchCardBindingModel_()
                                .id(launch.flightNumber)
                                .header("Launch")
                                .launch(launch)
                                .onLaunchClick { _ ->
                                    RocketDetailsFragmentDirections.rocketLaunchDetails(
                                        launch.flightNumber
                                    )
                                        .also { action ->
                                            findNavController().navigate(action)
                                        }
                                }
                        }
                    }
                }
                else -> Unit
            }
        }
    }
}