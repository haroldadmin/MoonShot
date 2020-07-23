package com.haroldadmin.moonshot.features.launchDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.base.safeArgs
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshotRepository.LinkPreviewUseCase
import com.haroldadmin.moonshotRepository.launch.GetLaunchDetailsUseCase
import com.haroldadmin.moonshotRepository.launch.GetLaunchPicturesUseCase
import com.haroldadmin.vector.VectorViewModelFactory
import com.haroldadmin.vector.ViewModelOwner
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class LaunchDetailsViewModel @AssistedInject constructor(
    @Assisted initState: LaunchDetailsState,
    private val launchDetailsUseCase: GetLaunchDetailsUseCase,
    private val launchPicturesUseCase: GetLaunchPicturesUseCase,
    private val linkPreviewUseCase: LinkPreviewUseCase
) : MoonShotViewModel<LaunchDetailsState>(initState) {

    init {
        viewModelScope.launch {
            getLaunchDetails(initState.flightNumber)
            getLaunchPictures(initState.flightNumber)
            getLinkPreviews()
        }
    }

    suspend fun getLaunchDetails(flightNumber: Int) {
        launchDetailsUseCase
            .getLaunchDetails(flightNumber)
            .collect { launchRes ->
                setState {
                    copy(launch = launchRes)
                }
            }
    }

    suspend fun getLaunchPictures(flightNumber: Int) {
        launchPicturesUseCase
            .getLaunchPictures(flightNumber)
            .collect { picturesRes ->
                setState {
                    copy(launchPictures = picturesRes)
                }
            }
    }

    fun getLinkPreviews() = withState { state ->
        viewModelScope.launch {
            state.launch()?.let { launchDetails ->
                linkPreviewUseCase
                    .getPreviews(launchDetails.linksToPreview)
                    .let { setState { copy(linkPreviews = it) } }
            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initState: LaunchDetailsState): LaunchDetailsViewModel
    }

    companion object : VectorViewModelFactory<LaunchDetailsViewModel, LaunchDetailsState> {
        override fun initialState(handle: SavedStateHandle, owner: ViewModelOwner): LaunchDetailsState? {
            val safeArgs = owner.safeArgs<LaunchDetailsFragmentArgs>()
            return LaunchDetailsState(
                safeArgs.flightNumber
            )
        }
    }
}
