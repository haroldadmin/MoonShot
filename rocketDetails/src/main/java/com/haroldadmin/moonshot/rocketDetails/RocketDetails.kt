package com.haroldadmin.moonshot.rocketDetails

import org.koin.core.context.loadKoinModules

object RocketDetails {
    private val loadModules by lazy {
        loadKoinModules(rocketDetailsModule)
    }

    fun init() = loadModules
}