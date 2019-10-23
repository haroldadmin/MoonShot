package com.haroldadmin.moonshot.rockets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.base.ComplexMoonShotFragment
import com.haroldadmin.moonshot.base.asyncTypedEpoxyController
import com.haroldadmin.moonshot.base.layoutAnimation
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.moonshot.rockets.databinding.FragmentRocketsBinding
import com.haroldadmin.moonshot.views.errorView
import com.haroldadmin.moonshot.views.loadingView
import com.haroldadmin.moonshot.views.rocketCard
import com.haroldadmin.vector.activityViewModel
import com.haroldadmin.vector.fragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.haroldadmin.moonshot.R as appR

class RocketsFragment : ComplexMoonShotFragment<RocketsViewModel, RocketsState>() {

    private lateinit var binding: FragmentRocketsBinding
    override val viewModel: RocketsViewModel by fragmentViewModel()
    private val mainViewModel: MainViewModel by activityViewModel()

    override fun initDI() = Rockets.init()

    override fun renderer(state: RocketsState) {
        epoxyController.setData(state)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRocketsBinding.inflate(inflater, container, false)

        mainViewModel.setTitle(getString(appR.string.title_rockets))

        binding.rvRockets.apply {
            setController(epoxyController)
            layoutAnimation = layoutAnimation(appR.anim.layout_animation_fade_in)
        }

        return binding.root
    }

    override val epoxyController by lazy {
        asyncTypedEpoxyController(viewModel) { state ->
            when (val rockets = state.rockets) {
                is Resource.Success -> {
                    rockets.data.forEach { rocket ->
                        rocketCard {
                            id(rocket.rocketId)
                            rocket(rocket)
                            onRocketClick { _ ->
                                RocketsFragmentDirections
                                    .rocketDetails(rocket.rocketId)
                                    .also { action ->
                                        findNavController().navigate(action)
                                    }
                            }
                        }
                    }
                }
                is Resource.Error<List<RocketMinimal>, *> -> {
                    errorView {
                        id("error-rockets")
                        errorText(getString(R.string.fragmentRocketsErrorText))
                    }
                    rockets.data?.forEach { rocket ->
                        rocketCard {
                            id(rocket.rocketId)
                            rocket(rocket)
                        }
                    }
                }
                else -> loadingView {
                    id("loading-rockets")
                    loadingText(getString(R.string.fragmentRocketsLoadingMessage))
                }
            }
        }
    }
}