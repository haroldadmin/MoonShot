package com.haroldadmin.moonshotRepository.mission

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.MissionDao
import com.haroldadmin.moonshot.models.Mission
import com.haroldadmin.moonshotRepository.SingleFetchNetworkBoundResource
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundResourceLazy
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.mission.MissionService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.haroldadmin.spacex_api_wrapper.mission.Mission as ApiMission
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMissionUseCase @Inject constructor(
    private val missionDao: MissionDao,
    private val missionService: MissionService,
    private val persister: PersistMissionUseCase
) {

    private var missionId: String = ""

    private val missionForIdResource: SingleFetchNetworkBoundResource<Mission, ApiMission, ErrorResponse> by singleFetchNetworkBoundResourceLazy(
        dbFetcher = { _, _, _ -> getMissionCached(missionId) },
        cacheValidator = { cached -> cached != null },
        apiFetcher = { getMissionFromApi(missionId) },
        dataPersister = { fetchedData -> persister.persist(fetchedData) }
    )

    @ExperimentalCoroutinesApi
    fun getMissionForId(id: String): Flow<Resource<Mission>> {
        missionId = id
        return missionForIdResource.flow()
    }

    private suspend fun getMissionCached(missionId: String) = withContext(Dispatchers.IO) {
        missionDao.forId(missionId)
    }

    private suspend fun getMissionFromApi(missionId: String) = withContext(Dispatchers.IO) {
        executeWithRetry {
            missionService.getMission(missionId).await()
        }
    }
}