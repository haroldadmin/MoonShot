package com.haroldadmin.moonshot.launchPad

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.airbnb.epoxy.EpoxyController
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.asyncTypedEpoxyController
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.databinding.FragmentLaunchpadBinding
import com.haroldadmin.moonshot.itemError
import com.haroldadmin.moonshot.itemExpandableTextWithHeading
import com.haroldadmin.moonshot.itemLaunchDetail
import com.haroldadmin.moonshot.itemLoading
import com.haroldadmin.moonshot.itemMapCard
import com.haroldadmin.moonshot.itemTextHeader
import com.haroldadmin.moonshot.itemTextWithHeading
import com.haroldadmin.moonshot.models.launchpad.LaunchPad
import com.haroldadmin.vector.withState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class LaunchPadFragment : MoonShotFragment() {

    private lateinit var binding: FragmentLaunchpadBinding
    private val safeArgs by navArgs<LaunchPadFragmentArgs>()
    private val differ by inject<Handler>(named("differ"))
    private val builder by inject<Handler>(named("builder"))
    private val viewModel by viewModel<LaunchPadViewModel> {
        val initialState = LaunchPadState(safeArgs.siteId)
        parametersOf(initialState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLaunchpadBinding.inflate(inflater, container, false)
        val animation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fade_in)
        binding.rvLaunchPad.apply {
            setController(epoxyController)
            layoutAnimation = animation
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, Observer { renderState() })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        epoxyController.cancelPendingModelBuild()
    }

    override fun renderState() = withState(viewModel) { state ->
        epoxyController.setData(state)
    }

    private val epoxyController by lazy {
        asyncTypedEpoxyController(builder, differ, viewModel) { state ->
            when (val launchpad = state.launchPad) {
                is Resource.Success -> buildLaunchPadModels(this, launchpad.data)
                is Resource.Error<LaunchPad, *> -> {
                    itemTextHeader {
                        id("map")
                        header(getString(R.string.itemMapCardMapHeader))
                    }
                    itemError {
                        id("launchpad-error")
                        error(getString(R.string.fragmentLaunchPadLaunchPadErrorMessage))
                    }

                    launchpad.data?.let { lp -> buildLaunchPadModels(this, lp) }
                }
                else -> itemLoading {
                    id("launchpad-loading")
                    message(getString(R.string.fragmentLaunchPadLaunchPadLoadingMessage))
                }
            }
        }
    }

    private fun buildLaunchPadModels(controller: EpoxyController, launchpad: LaunchPad) = with(controller) {
        itemLaunchDetail {
            id("launch-pad")
            detailHeader(getString(R.string.fragmentLaunchPadLaunchPadHeader))
            detailName(launchpad.siteNameLong)
        }
        itemExpandableTextWithHeading {
            id("launch-pad-detail")
            heading(getString(R.string.fragmentLaunchPadDetailsHeader))
            text(launchpad.details)
        }
        itemTextWithHeading {
            id("status")
            heading(getString(R.string.fragmentLaunchPadStatusHeader))
            text(launchpad.status.capitalize())
        }
        itemTextHeader {
            id("map")
            header(getString(R.string.itemMapCardMapHeader))
        }
        itemMapCard {
            id("map")
            mapImageUrl(launchpad.location.getStaticMapUrl())
            onMapClick { _ ->
                val mapIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("geo:${launchpad.location.latitude},${launchpad.location.longitude}")
                }
                startActivity(mapIntent)
            }
        }
    }
}