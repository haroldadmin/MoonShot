package com.haroldadmin.moonshot.launchPad

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.EpoxyController
import com.haroldadmin.moonshot.LaunchTypes
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.R as appR
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.asyncTypedEpoxyController
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.launchPad.databinding.FragmentLaunchpadBinding
import com.haroldadmin.moonshot.models.launchpad.LaunchPad
import com.haroldadmin.moonshot.views.detailCard
import com.haroldadmin.moonshot.views.errorView
import com.haroldadmin.moonshot.views.expandableTextView
import com.haroldadmin.moonshot.views.loadingView
import com.haroldadmin.moonshot.views.sectionHeaderView
import com.haroldadmin.moonshot.views.textCard
import com.haroldadmin.vector.activityViewModel
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
    private val mainViewModel: MainViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Launchpad.init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchpadBinding.inflate(inflater, container, false)
        mainViewModel.setTitle(getString(appR.string.title_launchpad))
        val animation =
            AnimationUtils.loadLayoutAnimation(requireContext(), appR.anim.layout_animation_fade_in)
        binding.rvLaunchPad.apply {
            setController(epoxyController)
            layoutAnimation = animation
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        renderState(viewModel) { state ->
            epoxyController.setData(state)
            if (state.launchPad is Resource.Success) {
                mainViewModel.setTitle(state.launchPad.data.siteNameLong)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        epoxyController.cancelPendingModelBuild()
    }

    private val epoxyController by lazy {
        asyncTypedEpoxyController(builder, differ, viewModel) { state ->
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
            itemMapCard {
                id("map")
                mapImageUrl(launchpad.location.getStaticMapUrl())
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                onMapClick { _ ->
                    val mapIntent = Intent(Intent.ACTION_VIEW).apply {
                        data =
                            Uri.parse("geo:${launchpad.location.latitude},${launchpad.location.longitude}")
                    }
                    startActivity(mapIntent)
                }
            }
        }
}