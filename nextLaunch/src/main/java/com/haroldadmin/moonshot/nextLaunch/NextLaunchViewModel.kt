package com.haroldadmin.moonshot.nextLaunch

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class NextLaunchViewModel @AssistedInject constructor(
    @Assisted initState: NextLaunchState,
    private val nextLaunchUseCase: GetNextLaunchUseCase
) : MoonShotViewModel<NextLaunchState>(initState) {

    init {
        viewModelScope.launch {
            getNextLaunch()
        }
    }

    suspend fun getNextLaunch() {
        nextLaunchUseCase
            .getNextLaunch()
            .collect { nextLaunchRes ->
                setState { copy(nextLaunch = nextLaunchRes) }
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initState: NextLaunchState): NextLaunchViewModel
    }
}
