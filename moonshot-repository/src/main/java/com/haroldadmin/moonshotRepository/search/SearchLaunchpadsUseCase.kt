package com.haroldadmin.moonshotRepository.search

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.LaunchPadDao
import com.haroldadmin.moonshot.models.SearchQuery
import com.haroldadmin.moonshot.models.LaunchPad
import com.haroldadmin.moonshotRepository.launchPad.PersistLaunchPadUseCase
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPadService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchLaunchpadsUseCase @Inject constructor(
    private val launchPadDao: LaunchPadDao,
    private val launchPadService: LaunchPadService,
    persistLaunchPadUseCase: PersistLaunchPadUseCase
) {

    private val res by searchResourceLazy(
        dbFetcher = { _, query, limit, offset -> getSearchResultsCached(query, limit, offset) },
        cacheValidator = { !it.isNullOrEmpty() },
        apiFetcher = { getAllLaunchPadsFromApi() },
        dataPersister = { launchPads -> persistLaunchPadUseCase.persistLaunchPads(launchPads) }
    )

    @ExperimentalCoroutinesApi
    fun searchFor(query: SearchQuery, limit: Int): Flow<Resource<List<LaunchPad>>> {
        res.updateParams(query, limit)
        return res.flow()
    }

    private suspend fun getSearchResultsCached(query: String, limit: Int, offset: Int) = withContext(Dispatchers.IO) {
        launchPadDao.forQuery(query, limit)
    }

    private suspend fun getAllLaunchPadsFromApi() = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchPadService.getAllLaunchPads().await()
        }
    }
}