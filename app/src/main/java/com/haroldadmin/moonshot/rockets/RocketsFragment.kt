package com.haroldadmin.moonshot.rockets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.simpleController
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.databinding.FragmentRocketsBinding
import com.haroldadmin.moonshot.launchItem
import com.haroldadmin.moonshot.models.rocket.Rocket
import kotlinx.android.synthetic.main.basic_row.*

class RocketsFragment : MoonShotFragment() {

    private lateinit var binding: FragmentRocketsBinding
    private val viewModel: RocketsViewModel by fragmentViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRocketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvLaunches.setController(epoxyController())
    }

    override fun epoxyController() = simpleController(viewModel) { state ->
        when (val rockets = state.rockets) {
            is Resource.Success -> {
                rockets.data.forEach { rocket ->
                    launchItem {
                        id(rocket.rocketId)
                        title(rocket.rocketName)
                        subtitle(rocket.description)
                    }
                }
            }
            is Resource.Error<List<Rocket>, *> -> {
                launchItem {
                    id("error-header")
                    title("Error loading rockets")
                }
                rockets.data?.forEach { rocket ->
                    launchItem {
                        id(rocket.rocketId)
                        title(rocket.rocketName)
                        subtitle(rocket.description)
                    }
                }
            }
            else -> launchItem {
                id("loading-header")
                title("Loading rockets")
                subtitle("Hang on")
            }
        }
    }

    override fun invalidate() {
        withState(viewModel) { state ->
            Log.d(this::class.java.simpleName, state.rockets.toString())
            binding.rvLaunches.requestModelBuild()
        }
    }
}