package com.haroldadmin.moonshot

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.haroldadmin.moonshot.base.SavedStateMoonShotViewModel
import com.haroldadmin.moonshot.core.Consumable
import com.haroldadmin.moonshot.core.asConsumable
import kotlinx.coroutines.launch

private const val KEY_TOOLBAR_TITLE = "toolbar-title"

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    initState: ScaffoldingState? = null
) : SavedStateMoonShotViewModel<ScaffoldingState>(initState, savedStateHandle) {

    init {
        val persistedTitle: String? = savedStateHandle[KEY_TOOLBAR_TITLE]
        if (persistedTitle == null) {
            setInitialState(ScaffoldingState(Consumable(null)))
        } else {
            setInitialState(ScaffoldingState(persistedTitle.asConsumable()))
        }
    }

    fun setTitle(title: String?) = viewModelScope.launch {
        if (title == null) return@launch
        val toolbarTitle = title.asConsumable()
        setState {
            copy(toolbarTitle = toolbarTitle)
        }
        savedStateHandle.set(KEY_TOOLBAR_TITLE, title)
    }
}

class MainViewModelFactory(
    savedStateRegistryOwner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(savedStateRegistryOwner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return MainViewModel(handle) as T
    }
}