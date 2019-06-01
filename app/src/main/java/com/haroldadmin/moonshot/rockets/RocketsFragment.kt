package com.haroldadmin.moonshot.rockets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.fragmentViewModel
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.simpleController
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.databinding.FragmentRocketsBinding
import com.haroldadmin.moonshot.itemError
import com.haroldadmin.moonshot.itemLoading
import com.haroldadmin.moonshot.itemRocket
import com.haroldadmin.moonshot.models.rocket.Rocket

class RocketsFragment : MoonShotFragment() {

    private lateinit var binding: FragmentRocketsBinding
    private val viewModel: RocketsViewModel by fragmentViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRocketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvRockets.setController(epoxyController())
    }

    override fun epoxyController() = simpleController(viewModel) { state ->
        when (val rockets = state.rockets) {
            is Resource.Success -> {
                rockets.data.forEach { rocket ->
                    itemRocket {
                        id(rocket.rocketId)
                        rocket(rocket)
                    }
                }
            }
            is Resource.Error<List<Rocket>, *> -> {
                itemError {
                    id("error-rockets")
                    error("Error loading rockets")
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
                message("Loading Rockets")
            }
        }
    }

    override fun invalidate() {
        binding.rvRockets.requestModelBuild()
    }
}