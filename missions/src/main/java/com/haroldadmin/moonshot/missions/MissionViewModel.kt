package com.haroldadmin.moonshot.missions

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.mission.GetMissionUseCase
import com.haroldadmin.vector.VectorViewModelFactory
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MissionViewModel @AssistedInject constructor(
    @Assisted initState: MissionState,
    private val missionUseCase: GetMissionUseCase
) : MoonShotViewModel<MissionState>(initState) {

    init {
        viewModelScope.launch {
            requireNotNull(initState.missionId)
            getMissionDetails(initState.missionId)
        }
    }

    private suspend fun getMissionDetails(missionId: String) {
        missionUseCase.getMissionForId(missionId).collect { missionRes ->
            setState { copy(mission = missionRes) }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initState: MissionState)
    }

    companion object : VectorViewModelFactory<MissionViewModel, MissionState> {
//        override fun initialState(handle: SavedStateHandle, owner: ViewModelOwner): MissionState? {
//            (owner as FragmentViewModelOwner).fragment<MissionFragment>()
//        }
    }
}