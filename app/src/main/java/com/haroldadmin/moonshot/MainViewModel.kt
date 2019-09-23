package com.haroldadmin.moonshot

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.SavedStateMoonShotViewModel
import com.haroldadmin.moonshot.core.Consumable
import com.haroldadmin.moonshot.core.asConsumable
import com.haroldadmin.vector.VectorViewModelFactory
import com.haroldadmin.vector.ViewModelOwner
import kotlinx.coroutines.launch

class MainViewModel(
    initState: ScaffoldingState,
    savedStateHandle: SavedStateHandle
) : SavedStateMoonShotViewModel<ScaffoldingState>(initState, savedStateHandle) {

    fun setTitle(title: String?) = viewModelScope.launch {
        if (title == null) return@launch
        val toolbarTitle = title.asConsumable()
        setStateAndPersist {
            copy(toolbarTitle = toolbarTitle)
        }
    }

    companion object: VectorViewModelFactory<MainViewModel, ScaffoldingState> {
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
