package com.haroldadmin.moonshot.rockets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.base.ComplexMoonShotFragment
import com.haroldadmin.moonshot.base.asyncController
import com.haroldadmin.moonshot.base.layoutAnimation
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.di.appComponent
import com.haroldadmin.moonshot.models.Rocket
import com.haroldadmin.moonshot.rockets.databinding.FragmentRocketsBinding
import com.haroldadmin.moonshot.rockets.di.DaggerRocketsComponent
import com.haroldadmin.moonshot.views.errorView
import com.haroldadmin.moonshot.views.loadingView
import com.haroldadmin.moonshot.views.rocketCard
import com.haroldadmin.vector.activityViewModel
import com.haroldadmin.vector.fragmentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import com.haroldadmin.moonshot.R as appR

@ExperimentalCoroutinesApi
class RocketsFragment : ComplexMoonShotFragment<RocketsViewModel, RocketsState>() {

    private lateinit var binding: FragmentRocketsBinding

    @Inject lateinit var viewModelFactory: RocketsViewModel.Factory
    @Inject lateinit var mainViewModelFactory: MainViewModel.Factory

    override val viewModel: RocketsViewModel by fragmentViewModel { initState, _ ->
        viewModelFactory.create(initState)
    }
    private val mainViewModel: MainViewModel by activityViewModel { initState, savedStateHandle ->
        mainViewModelFactory.create(initState, savedStateHandle)
    }

    override fun initDI() {
        DaggerRocketsComponent.builder()
            .appComponent(appComponent())
            .build()
            .inject(this)
    }

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

    override fun epoxyController() = asyncController(viewModel) { state ->
        when (val rockets = state.rockets) {
            is Resource.Success -> {
                rockets().forEach { rocket ->
                    rocketCard {
                        id(rocket.rocketId)
                        rocket(rocket)
                        onRocketClick { _ ->
                            val action = RocketsFragmentDirections.rocketDetails(rocket.rocketId)
                            findNavController().navigate(action)
                        }
                    }
                }
            }
            is Resource.Error<List<Rocket>, *> -> {
                errorView {
                    id("error-rockets")
                    errorText(getString(R.string.fragmentRocketsErrorText))
                }
                rockets()?.forEach { rocket ->
                    rocketCard {
                        id(rocket.rocketId)
                        rocket(rocket)
                    }
                }
            }
            is Resource.Loading -> loadingView {
                id("loading-rockets")
                loadingText(getString(R.string.fragmentRocketsLoadingMessage))
            }
            else -> Unit
        }
    }
}