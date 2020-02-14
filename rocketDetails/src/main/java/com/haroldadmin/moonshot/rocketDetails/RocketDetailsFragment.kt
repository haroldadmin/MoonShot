package com.haroldadmin.moonshot.rocketDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.base.ComplexMoonShotFragment
import com.haroldadmin.moonshot.base.asyncController
import com.haroldadmin.moonshot.base.layoutAnimation
import com.haroldadmin.moonshot.base.withModelsFrom
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.databinding.FragmentRocketDetailsBinding
import com.haroldadmin.moonshot.di.appComponent
import com.haroldadmin.moonshot.models.Rocket
import com.haroldadmin.moonshot.rocketDetails.di.DaggerRocketDetailsComponent
import com.haroldadmin.moonshot.utils.formatNumber
import com.haroldadmin.moonshot.views.LaunchCardModel_
import com.haroldadmin.moonshot.views.detailCard
import com.haroldadmin.moonshot.views.errorView
import com.haroldadmin.moonshot.views.expandableTextView
import com.haroldadmin.moonshot.views.loadingView
import com.haroldadmin.moonshot.views.rocketCard
import com.haroldadmin.moonshot.views.sectionHeaderView
import com.haroldadmin.vector.activityViewModel
import com.haroldadmin.vector.fragmentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import com.haroldadmin.moonshot.R as appR

@ExperimentalCoroutinesApi
class RocketDetailsFragment : ComplexMoonShotFragment<RocketDetailsViewModel, RocketDetailsState>() {

    private var _binding: FragmentRocketDetailsBinding? = null
    private val binding: FragmentRocketDetailsBinding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: RocketDetailsViewModel.Factory
    @Inject
    lateinit var mainViewModelFactory: MainViewModel.Factory

    override val viewModel: RocketDetailsViewModel by fragmentViewModel { initState, _ ->
        viewModelFactory.create(initState)
    }

    private val mainViewModel: MainViewModel by activityViewModel { initState, savedStateHandle ->
        mainViewModelFactory.create(initState, savedStateHandle)
    }

    override fun initDI() {
        DaggerRocketDetailsComponent.builder()
            .appComponent(appComponent())
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRocketDetailsBinding.inflate(inflater, container, false)

        mainViewModel.setTitle(getString(appR.string.title_rocket_details))

        binding.rvRocketDetails.apply {
            setController(epoxyController)
            layoutAnimation = layoutAnimation(appR.anim.layout_animation_fade_in)
        }

        return binding.root
    }

    override fun renderer(state: RocketDetailsState) {
        epoxyController.setData(state)
        state.rocket()?.let { mainViewModel.setTitle(it.rocketName) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun epoxyController() = asyncController(viewModel) { state ->
        when (val rocket = state.rocket) {
            is Resource.Success -> buildRocketModels(rocket())
            is Resource.Error<Rocket, *> -> {
                errorView {
                    id("rocket-details-error")
                    errorText(getString(R.string.rocketDetailsFragmentRocketDetailsErrorMessage))
                }
                rocket()?.let { buildRocketModels(it) }
            }
            is Resource.Loading -> loadingView {
                id("rocket-details-loading")
                loadingText(getString(R.string.rocketDetailsFragmentRocketLoadingMessage))
            }
            else -> Unit
        }

        when (val launches = state.launches) {
            is Resource.Success -> {
                if (launches.data.isEmpty()) return@asyncController

                sectionHeaderView {
                    id("launches-header")
                    header(getString(R.string.rocketDetailsFragmentLaunchesHeader))
                }

                carousel {
                    id("rocket-launches")
                    withModelsFrom(launches.data) { launch ->
                        LaunchCardModel_()
                            .id(launch.flightNumber)
                            .header("Launch")
                            .launch(launch)
                            .onLaunchClick { _ ->
                                val action = RocketDetailsFragmentDirections.rocketLaunchDetails(launch.flightNumber)
                                findNavController().navigate(action)
                            }
                    }
                }
            }
            else -> Unit
        }
    }

    private fun EpoxyController.buildRocketModels(rocket: Rocket) = with(rocket) {
        rocketCard {
            id(rocketId)
            rocket(this@with)
        }
        expandableTextView {
            id("description")
            header(getString(com.haroldadmin.moonshot.rocketDetails.R.string.rocketDetailsFragmentRocketDescriptionHeader))
            content(description)
        }
        detailCard {
            id("cost-per-launch")
            header(getString(com.haroldadmin.moonshot.rocketDetails.R.string.rocketDetailsFragmentCostPerLaunchHeader))
            content("\$ ${formatNumber(costPerLaunch)}")
        }
        detailCard {
            id("success-percentage")
            header(getString(com.haroldadmin.moonshot.rocketDetails.R.string.rocketDetailsFragmentSuccessPercentageHeader))
            content(successRatePercentage.toString())
        }
    }

    private fun formatNumber(number: Long): String {
        return requireContext().formatNumber(number)
    }
}