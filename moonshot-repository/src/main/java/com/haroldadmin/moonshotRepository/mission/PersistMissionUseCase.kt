package com.haroldadmin.moonshotRepository.mission

import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.database.MissionDao
import com.haroldadmin.moonshot.models.Mission
import com.haroldadmin.moonshotRepository.mappers.toDbMission
import com.haroldadmin.spacex_api_wrapper.mission.Mission as ApiMission
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersistMissionUseCase @Inject constructor(
    private val missionDao: MissionDao,
    private val appDispatchers: AppDispatchers
) {

    suspend fun persist(mission: Mission) = withContext(appDispatchers.IO) {
        missionDao.save(mission)
    }

    suspend fun persist(mission: ApiMission) = withContext(appDispatchers.IO) {
        missionDao.save(mission.toDbMission())
    }

    suspend fun persist(missions: List<Mission>, shouldSynchronize: Boolean = false) = withContext(appDispatchers.IO) {
        if (shouldSynchronize) {
            missionDao.synchronizeBlocking(missions)
        } else {
            missionDao.saveAll(missions)
        }
    }

    @JvmName("persistApiMissions")
    suspend fun persist(missions: List<ApiMission>, shouldSynchronize: Boolean = false) = withContext(appDispatchers.IO) {
        if (shouldSynchronize) {
            missionDao.synchronizeBlocking(
                missions.map { it.toDbMission() }
            )
        } else {
            missionDao.saveAll(missions.map { it.toDbMission() })
        }
    }
}