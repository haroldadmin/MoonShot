package com.haroldadmin.moonshotRepository.launchPad

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.LaunchPadDao
import com.haroldadmin.moonshot.models.LaunchPad
import com.haroldadmin.moonshotRepository.SingleFetchNetworkBoundResource
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundFlow
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundResource
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPadService
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPad as ApiLaunchPad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetLaunchPadUseCase(
    private val launchPadDao: LaunchPadDao,
    private val launchPadService: LaunchPadService,
    private val persistLaunchPadUseCase: PersistLaunchPadUseCase
) {

    private lateinit var launchPadResource: SingleFetchNetworkBoundResource<LaunchPad, ApiLaunchPad, ErrorResponse>
    private lateinit var allLaunchPadsResource: SingleFetchNetworkBoundResource<List<LaunchPad>, List<ApiLaunchPad>, ErrorResponse>

    @ExperimentalCoroutinesApi
    fun getLaunchPad(siteId: String): Flow<Resource<LaunchPad>> {
        if (!::launchPadResource.isInitialized) {
            launchPadResource = singleFetchNetworkBoundResource(
                dbFetcher = { getLaunchPadCached(siteId) },
                cacheValidator = { cached -> cached != null },
                apiFetcher = { getLaunchPadFromApi(siteId) },
                dataPersister = persistLaunchPadUseCase::persistLaunchPad
            )
        }
        return launchPadResource.flow()
    }

    @ExperimentalCoroutinesApi
    fun getAllLaunchPads(limit: Int = Int.MAX_VALUE, offset: Int = 0): Flow<Resource<List<LaunchPad>>> {
        if (!::allLaunchPadsResource.isInitialized) {
            allLaunchPadsResource = singleFetchNetworkBoundResource(
                dbFetcher = { getLaunchPadsCached(limit, offset) },
                cacheValidator = { cached -> !cached.isNullOrEmpty() },
                apiFetcher = { getLaunchPadsFromApi(limit) },
                dataPersister = { launchPads -> persistLaunchPadUseCase.persistLaunchPads(launchPads, false) }
            )
        }
        return allLaunchPadsResource.flow()
    }

    @ExperimentalCoroutinesApi
    suspend fun sync(): Resource<Unit> = when (val apiLaunchPads = getLaunchPadsFromApi()) {
        is NetworkResponse.Success -> {
            persistLaunchPadUseCase.persistLaunchPads(apiLaunchPads.body, shouldSynchronize = true)
            Resource.Success(Unit)
        }
        else -> Resource.Error(Unit, null)
    }

    private suspend fun getLaunchPadFromApi(siteId: String) = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchPadService.getLaunchPad(siteId).await()
        }
    }

    private suspend fun getLaunchPadCached(siteId: String) = withContext(Dispatchers.IO) {
        launchPadDao.one(siteId)
    }

    private suspend fun getLaunchPadsFromApi(limit: Int? = null) = withContext(Dispatchers.IO) {
        executeWithRetry { launchPadService.getAllLaunchPads(limit).await() }
    }

    private suspend fun getLaunchPadsCached(limit: Int, offset: Int) = withContext(Dispatchers.IO) {
        launchPadDao.all(limit, offset)
    }
}