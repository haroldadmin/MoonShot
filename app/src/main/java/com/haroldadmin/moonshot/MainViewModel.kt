package com.haroldadmin.moonshot

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.SavedStateMoonShotViewModel
import com.haroldadmin.moonshot.core.Consumable
import com.haroldadmin.moonshot.core.asConsumable
import com.haroldadmin.vector.VectorViewModelFactory
import com.haroldadmin.vector.ViewModelOwner
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch

class MainViewModel @AssistedInject constructor(
    @Assisted initState: ScaffoldingState,
    @Assisted handle: SavedStateHandle
) : SavedStateMoonShotViewModel<ScaffoldingState>(initState, handle) {

    fun setTitle(title: String?) = viewModelScope.launch {
        if (title == null) return@launch
        val toolbarTitle = title.asConsumable()
        setStateAndPersist {
            copy(toolbarTitle = toolbarTitle)
        }
    }

    fun hideScaffolding() = setState {
        copy(shouldHideScaffolding = true)
    }

    fun showScaffolding() = setState {
        copy(shouldHideScaffolding = false)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initState: ScaffoldingState, handle: SavedStateHandle): MainViewModel
    }

    companion object : VectorViewModelFactory<MainViewModel, ScaffoldingState> {
        override fun initialState(
            handle: SavedStateHandle,
            owner: ViewModelOwner
        ): ScaffoldingState? {
            val persistedState: ScaffoldingState? = handle[KEY_SAVED_STATE]
            return if (persistedState == null) {
                ScaffoldingState(Consumable(null))
            } else {
                ScaffoldingState(persistedState.toolbarTitle)
            }
        }
    }
}
