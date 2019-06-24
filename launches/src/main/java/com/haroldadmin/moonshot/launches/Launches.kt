package com.haroldadmin.moonshot.launches

import org.koin.core.context.loadKoinModules

object Launches {
    private val loadModules: Unit by lazy {
        loadKoinModules(launchesModule)
    }

    fun init() = loadModules
}