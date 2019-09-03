package com.haroldadmin.moonshot.rockets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.R as appR
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.typedEpoxyController
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.itemError
import com.haroldadmin.moonshot.itemLoading
import com.haroldadmin.moonshot.itemRocket
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.moonshot.rockets.databinding.FragmentRocketsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RocketsFragment : MoonShotFragment() {

    private lateinit var binding: FragmentRocketsBinding
    private val viewModel by viewModel<RocketsViewModel> {
        parametersOf(RocketsState())
    }
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Rockets.init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRocketsBinding.inflate(inflater, container, false)
        mainViewModel.setTitle(getString(appR.string.title_rockets))
        val animation =
            AnimationUtils.loadLayoutAnimation(requireContext(), appR.anim.layout_animation_fade_in)
        binding.rvRockets.apply {
            setController(epoxyController)
            layoutAnimation = animation
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        renderState(viewModel) {
            epoxyController.setData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        epoxyController.cancelPendingModelBuild()
    }

    private val epoxyController by lazy {
        typedEpoxyController(viewModel) { state ->
            when (val rockets = state.rockets) {
                is Resource.Success -> {
                    rockets.data.forEach { rocket ->
                        itemRocket {
                            id(rocket.rocketId)
                            rocket(rocket)
                            onRocketClick { _ ->
                                RocketsFragmentDirections.rocketDetails(rocket.rocketId)
                                    .also { action ->
                                        findNavController().navigate(action)
                                    }
                            }
                        }
                    }
                }
                is Resource.Error<List<RocketMinimal>, *> -> {
                    itemError {
                        id("error-rockets")
                        error(getString(R.string.fragmentRocketsErrorText))
                    }
                    rockets.data?.forEach { rocket ->
                        itemRocket {
                            id(rocket.rocketId)
                            rocket(rocket)
                        }
                    }
                }
                else -> itemLoading {
                    id("loading-rockets")
                    message(getString(R.string.fragmentRocketsLoadingMessage))
                }
            }
        }
    }
}