package com.haroldadmin.moonshot.launchPad

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.EpoxyController
import com.haroldadmin.moonshot.LaunchTypes
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.base.ComplexMoonShotFragment
import com.haroldadmin.moonshot.base.asyncTypedEpoxyController
import com.haroldadmin.moonshot.base.layoutAnimation
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.launchPad.databinding.FragmentLaunchpadBinding
import com.haroldadmin.moonshot.launchPad.views.mapCard
import com.haroldadmin.moonshot.models.launchpad.LaunchPad
import com.haroldadmin.moonshot.views.detailCard
import com.haroldadmin.moonshot.views.errorView
import com.haroldadmin.moonshot.views.expandableTextView
import com.haroldadmin.moonshot.views.loadingView
import com.haroldadmin.moonshot.views.sectionHeaderView
import com.haroldadmin.moonshot.views.textCard
import com.haroldadmin.vector.activityViewModel
import com.haroldadmin.vector.fragmentViewModel
import com.haroldadmin.moonshot.R as appR

class LaunchPadFragment : ComplexMoonShotFragment<LaunchPadViewModel, LaunchPadState>() {

    private lateinit var binding: FragmentLaunchpadBinding
    override val viewModel: LaunchPadViewModel by fragmentViewModel()
    private val mainViewModel: MainViewModel by activityViewModel()

    override fun initDI() = Launchpad.init()

    override fun renderer(state: LaunchPadState) {
        epoxyController.setData(state)
        if (state.launchPad is Resource.Success) {
            mainViewModel.setTitle(state.launchPad.data.siteNameLong)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchpadBinding.inflate(inflater, container, false)

        mainViewModel.setTitle(getString(appR.string.title_launchpad))

        binding.rvLaunchPad.apply {
            setController(epoxyController)
            layoutAnimation = layoutAnimation(appR.anim.layout_animation_fade_in)
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        return binding.root
    }

    override val epoxyController by lazy {
        asyncTypedEpoxyController(viewModel) { state ->
            when (val launchpad = state.launchPad) {
                is Resource.Success -> buildLaunchPadModels(this, launchpad.data)
                is Resource.Error<LaunchPad, *> -> {
                    sectionHeaderView {
                        id("map")
                        header(getString(R.string.itemMapCardMapHeader))
                    }
                    errorView {
                        id("launchpad-error")
                        errorText(getString(R.string.fragmentLaunchPadLaunchPadErrorMessage))
                        spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                    }

                    launchpad.data?.let { lp -> buildLaunchPadModels(this, lp) } ?: Unit
                }
                else -> loadingView {
                    id("launchpad-loading")
                    loadingText(getString(R.string.fragmentLaunchPadLaunchPadLoadingMessage))
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
        }
    }

    private fun buildLaunchPadModels(controller: EpoxyController, launchpad: LaunchPad) =
        with(controller) {
            detailCard {
                id("launch-pad")
                header(getString(R.string.fragmentLaunchPadLaunchPadHeader))
                content(launchpad.siteNameLong)
            }
            expandableTextView {
                id("launch-pad-detail")
                header(getString(R.string.fragmentLaunchPadDetailsHeader))
                content(launchpad.details)
            }
            textCard {
                id("status")
                header(getString(R.string.fragmentLaunchPadStatusHeader))
                content(launchpad.status.capitalize())
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount / 2 }
            }
            textCard {
                id("success-percentage")
                header("Success Rate")
                content(launchpad.successPercentage)
                onTextClick { _ ->
                    LaunchPadFragmentDirections.launchPadLaunches(
                        type = LaunchTypes.LAUNCHPAD,
                        siteId = launchpad.siteId
                    ).also { action ->
                        findNavController().navigate(action)
                    }
                }
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount / 2 }
            }
            sectionHeaderView {
                id("map")
                header(getString(R.string.itemMapCardMapHeader))
            }
            mapCard {
                id("map")
                mapImageUrl(launchpad.location.getStaticMapUrl())
                onMapClick { _ ->
                    val uri = Uri.parse("geo:${launchpad.location.latitude},${launchpad.location.longitude}")
                    val mapIntent = Intent(Intent.ACTION_VIEW).apply {
                        data = uri
                    }
                    startActivity(mapIntent)
                }
            }
        }
}