package com.haroldadmin.moonshot

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.core.Consumable
import kotlinx.coroutines.launch

class MainViewModel(
    initState: ScaffoldingState
) : MoonShotViewModel<ScaffoldingState>(initState) {

    fun setTitle(title: String) = viewModelScope.launch {
        val toolbarTitle = Consumable(title)
        setState {
            copy(toolbarTitle = toolbarTitle)
        }
    }
}