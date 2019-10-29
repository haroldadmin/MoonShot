package com.haroldadmin.moonshotRepository.launchPad

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.pairOf
import com.haroldadmin.moonshot.database.LaunchPadDao
import com.haroldadmin.moonshot.models.LaunchPad
import com.haroldadmin.moonshotRepository.SingleFetchNetworkBoundResource
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundResourceLazy
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

    private val defaultLimit = 15
    private val defaultOffset = 0
    private var siteId = ""

    @ExperimentalCoroutinesApi
    private val launchPadResource: SingleFetchNetworkBoundResource<LaunchPad, ApiLaunchPad, ErrorResponse> by singleFetchNetworkBoundResourceLazy(
        initialParams = ::initialParams,
        dbFetcher = { _, _, _ -> getLaunchPadCached(siteId) },
        cacheValidator = { cached -> cached != null },
        apiFetcher = { getLaunchPadFromApi(siteId) },
        dataPersister = persistLaunchPadUseCase::persistLaunchPad
    )

    @ExperimentalCoroutinesApi
    private val allLaunchPadsResource: SingleFetchNetworkBoundResource<List<LaunchPad>, List<ApiLaunchPad>, ErrorResponse> by singleFetchNetworkBoundResourceLazy(
        initialParams = ::initialParams,
        dbFetcher = { _, limit, offset -> getLaunchPadsCached(limit, offset) },
        cacheValidator = { cached -> !cached.isNullOrEmpty() },
        apiFetcher = { getLaunchPadsFromApi() },
        dataPersister = { launchPads -> persistLaunchPadUseCase.persistLaunchPads(launchPads, false) }
    )

    @ExperimentalCoroutinesApi
    fun getLaunchPad(siteId: String): Flow<Resource<LaunchPad>> {
        this.siteId = siteId
        return launchPadResource.flow()
    }

    @ExperimentalCoroutinesApi
    fun getAllLaunchPads(limit: Int = defaultLimit, offset: Int = defaultOffset): Flow<Resource<List<LaunchPad>>> {
        allLaunchPadsResource.updateParams(limit, offset)
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

    private suspend fun getLaunchPadsFromApi() = withContext(Dispatchers.IO) {
        executeWithRetry { launchPadService.getAllLaunchPads().await() }
    }

    private suspend fun getLaunchPadsCached(limit: Int, offset: Int) = withContext(Dispatchers.IO) {
        launchPadDao.all(limit, offset)
    }

    private fun initialParams() = pairOf(defaultLimit, defaultOffset)
}