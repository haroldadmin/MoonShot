package com.haroldadmin.moonshotRepository.launchPad

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.LaunchPadDao
import com.haroldadmin.moonshot.models.LaunchPad
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundFlow
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPadService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetLaunchPadUseCase(
    private val launchPadDao: LaunchPadDao,
    private val launchPadService: LaunchPadService,
    private val persistLaunchPadUseCase: PersistLaunchPadUseCase
) {

    @ExperimentalCoroutinesApi
    fun getLaunchPad(siteId: String): Flow<Resource<LaunchPad>> {
        return singleFetchNetworkBoundFlow(
            dbFetcher = { getLaunchPadCached(siteId) },
            cacheValidator = { cached -> cached != null },
            apiFetcher = { getLaunchPadFromApi(siteId) },
            dataPersister = persistLaunchPadUseCase::persistLaunchPad
        )
    }

    @ExperimentalCoroutinesApi
    fun getAllLaunchPads(limit: Int = Int.MAX_VALUE, offset: Int = 0): Flow<Resource<List<LaunchPad>>> {
        return singleFetchNetworkBoundFlow(
            dbFetcher = { getLaunchPadsCached(limit, offset) },
            cacheValidator = { cached -> !cached.isNullOrEmpty() },
            apiFetcher = { getLaunchPadsFromApi(limit) },
            dataPersister = { launchPads -> persistLaunchPadUseCase.persistLaunchPads(launchPads, false) }
        )
    }

    private suspend fun getLaunchPadFromApi(siteId: String) = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchPadService.getLaunchPad(siteId).await()
        }
    }

    private suspend fun getLaunchPadCached(siteId: String) = withContext(Dispatchers.IO) {
        launchPadDao.one(siteId)
    }

    private suspend fun getLaunchPadsFromApi(limit: Int) = withContext(Dispatchers.IO) {
        executeWithRetry { launchPadService.getAllLaunchPads(limit).await() }
    }

    private suspend fun getLaunchPadsCached(limit: Int, offset: Int) = withContext(Dispatchers.IO) {
        launchPadDao.all(limit, offset)
    }
}