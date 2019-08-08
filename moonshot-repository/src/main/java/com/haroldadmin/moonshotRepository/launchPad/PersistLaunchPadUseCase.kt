package com.haroldadmin.moonshotRepository.launchPad

import com.haroldadmin.moonshot.database.launchPad.LaunchPadDao
import com.haroldadmin.moonshotRepository.mappers.toDbLaunchPad
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPad

class PersistLaunchPadUseCase(private val launchPadDao: LaunchPadDao) {

    suspend fun persistLaunchPad(apiLaunchPad: LaunchPad) {
        launchPadDao.save(apiLaunchPad.toDbLaunchPad())
    }

    suspend fun persistLaunchPads(apiLaunchPads: List<LaunchPad>) {
        launchPadDao.saveAll(apiLaunchPads.map { it.toDbLaunchPad() })
    }
}