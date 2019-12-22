package com.haroldadmin.moonshot.missions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.base.safeArgs
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshotRepository.LinkPreviewUseCase
import com.haroldadmin.moonshotRepository.mission.GetMissionUseCase
import com.haroldadmin.vector.VectorViewModelFactory
import com.haroldadmin.vector.ViewModelOwner
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MissionViewModel @AssistedInject constructor(
    @Assisted initState: MissionState,
    private val missionUseCase: GetMissionUseCase,
    private val linkPreviewUseCase: LinkPreviewUseCase
) : MoonShotViewModel<MissionState>(initState) {

    init {
        viewModelScope.launch {
            requireNotNull(initState.missionId)
            getMissionDetails(initState.missionId)
            getLinkPreviews()
        }
    }

    private suspend fun getMissionDetails(missionId: String) {
        missionUseCase.getMissionForId(missionId).collect { missionRes ->
            setState { copy(mission = missionRes) }
        }
    }

    private fun getLinkPreviews() = withState { state ->
        viewModelScope.launch {
            state.mission()?.let { mission ->
                linkPreviewUseCase
                    .getPreviews(mission.linksToPreview)
                    .let {
                        setState { copy(linkPreviews = it) }
                    }
            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initState: MissionState): MissionViewModel
    }

    companion object : VectorViewModelFactory<MissionViewModel, MissionState> {
        override fun initialState(handle: SavedStateHandle, owner: ViewModelOwner): MissionState? {
            val args = owner.safeArgs<MissionFragmentArgs>()
            return MissionState(missionId = args.missionId)
        }
    }
}