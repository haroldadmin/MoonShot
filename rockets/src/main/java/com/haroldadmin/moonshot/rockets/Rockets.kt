package com.haroldadmin.moonshot.rockets

import org.koin.core.context.loadKoinModules

object Rockets {
    private val loadModules: Unit by lazy {
        loadKoinModules(rocketsModule)
    }

    fun init() = loadModules
}