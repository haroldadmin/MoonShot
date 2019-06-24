package com.haroldadmin.moonshot.launchDetails

import org.koin.core.context.loadKoinModules

object LaunchDetails {
    private val loadModules: Unit by lazy {
        loadKoinModules(launchDetailsModule)
    }

    fun init() = loadModules
}