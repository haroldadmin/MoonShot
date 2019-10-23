package com.haroldadmin.moonshotRepository.launchPad

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launchPad.LaunchPadDao
import com.haroldadmin.moonshot.models.launchpad.LaunchPad
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundFlow
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPadService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetLaunchPadUseCase(
    private val launchPadDao: LaunchPadDao,
    private val launchPadService: LaunchPadService,
    private val persistLaunchPadUseCase: PersistLaunchPadUseCase
) {

    suspend fun getLaunchPad(siteId: String): Flow<Resource<LaunchPad>> {
        return singleFetchNetworkBoundFlow(
            dbFetcher = { getLaunchPadCached(siteId) },
            cacheValidator = { cached -> cached != null },
            apiFetcher = { getLaunchPadFromApi(siteId) },
            dataPersister = persistLaunchPadUseCase::persistLaunchPad
        )
    }

    private suspend fun getLaunchPadFromApi(siteId: String) = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchPadService.getLaunchPad(siteId).await()
        }
    }

    private suspend fun getLaunchPadCached(siteId: String) = withContext(Dispatchers.IO) {
        launchPadDao.getLaunchPad(siteId)
    }
}