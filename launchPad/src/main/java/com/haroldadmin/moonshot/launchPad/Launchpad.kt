package com.haroldadmin.moonshot.launchPad

import org.koin.core.context.loadKoinModules

object Launchpad {
    private val loadModules by lazy {
        loadKoinModules(launchPadModule)
    }

    fun init() = loadModules
}