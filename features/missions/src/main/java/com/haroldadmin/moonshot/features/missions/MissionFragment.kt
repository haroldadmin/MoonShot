package com.haroldadmin.moonshot.features.missions

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyController
import com.haroldadmin.moonshot.base.ComplexMoonShotFragment
import com.haroldadmin.moonshot.base.asyncController
import com.haroldadmin.moonshot.base.carousel
import com.haroldadmin.moonshot.base.layoutAnimation
import com.haroldadmin.moonshot.base.withModelsFromIndexed
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.di.appComponent
import com.haroldadmin.moonshot.features.missions.databinding.FragmentMissionBinding
import com.haroldadmin.moonshot.models.Mission
import com.haroldadmin.moonshot.views.LinkPreviewCardModel_
import com.haroldadmin.moonshot.views.detailCard
import com.haroldadmin.moonshot.views.errorView
import com.haroldadmin.moonshot.views.expandableTextView
import com.haroldadmin.moonshot.views.loadingView
import com.haroldadmin.moonshot.views.sectionHeaderView
import com.haroldadmin.vector.fragmentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import com.haroldadmin.moonshot.R as appR

@ExperimentalCoroutinesApi
class MissionFragment : ComplexMoonShotFragment<MissionViewModel, MissionState>() {

    private var _binding: FragmentMissionBinding? = null
    private val binding: FragmentMissionBinding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: MissionViewModel.Factory

    override val viewModel: MissionViewModel by fragmentViewModel { initState, _ ->
        viewModelFactory.create(initState)
    }

    override fun initDI() {
        DaggerMissionComponent.builder()
            .appComponent(appComponent())
            .build()
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMissionBinding.inflate(inflater, container, false)

        binding.rvMission.apply {
            setController(epoxyController)
            layoutAnimation = layoutAnimation(appR.anim.layout_animation_fade_in)
        }

        return binding.root
    }

    override fun renderer(state: MissionState) = epoxyController.setData(state)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun epoxyController() = asyncController(viewModel) { state ->
        when (val mission = state.mission) {
            is Resource.Success -> buildMissionModels(mission())
            is Resource.Error<Mission, *> -> {
                errorView {
                    id("mission-error")
                    errorText("Error fetching launch details")
                }

                mission()?.let { buildMissionModels(it) }
            }
            is Resource.Loading -> loadingView {
                id("mission-loading")
                loadingText("Loading mission details")
            }
            else -> Unit
        }

        if (state.linkPreviews.isNotEmpty()) {
            sectionHeaderView {
                id("mission-links-header")
                header(getString(R.string.missionLinksSectionHeader))
            }
            carousel {
                id("mission-links")
                withModelsFromIndexed(state.linkPreviews) { index, linkPreview ->
                    LinkPreviewCardModel_()
                        .id(index)
                        .linkPreview(linkPreview)
                        .onLinkClick {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkPreview.url))
                            startActivity(intent)
                        }
                }
            }
        }
    }

    private fun EpoxyController.buildMissionModels(mission: Mission) {
        detailCard {
            id("mission-name")
            content(mission.name)
            header(R.string.missionNameHeaderText)
            icon(R.drawable.ic_round_info_24)
        }

        expandableTextView {
            id("mission-description")
            content(mission.description ?: getString(R.string.noMissionDescriptionText))
            header(R.string.missionDescriptionHeaderText)
        }
    }
}