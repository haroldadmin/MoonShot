package com.haroldadmin.moonshot.nextLaunch

import org.koin.core.context.loadKoinModules

object NextLaunch {
    private val loadModules: Unit by lazy {
        loadKoinModules(nextLaunchModule)
    }

    fun init() = loadModules
}