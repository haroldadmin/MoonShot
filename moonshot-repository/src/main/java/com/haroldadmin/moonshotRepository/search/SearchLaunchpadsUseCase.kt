package com.haroldadmin.moonshotRepository.search

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.LaunchPadDao
import com.haroldadmin.moonshot.database.SearchQuery
import com.haroldadmin.moonshot.models.LaunchPad
import com.haroldadmin.moonshotRepository.launchPad.PersistLaunchPadUseCase
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPadService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SearchLaunchpadsUseCase(
    private val launchPadDao: LaunchPadDao,
    private val launchPadService: LaunchPadService,
    persistLaunchPadUseCase: PersistLaunchPadUseCase
) : SearchUseCase() {

    @ExperimentalCoroutinesApi
    private val resource = searchResource(
        dbQuery = { _query },
        dbLimit = { _limit },
        dbFetcher = { _, query, limit -> getSearchResultsCached(query, limit) },
        cacheValidator = { cached -> !cached.isNullOrEmpty() },
        apiFetcher = { getAllLaunchPadsFromApi() },
        dataPersister = { launchPads -> persistLaunchPadUseCase.persistLaunchPads(launchPads) }
    )

    @ExperimentalCoroutinesApi
    fun searchFor(query: SearchQuery, limit: Int): Flow<Resource<List<LaunchPad>>> {
        _query = query.sqlQuery()
        _limit = limit
        return resource.flow()
    }

    private suspend fun getSearchResultsCached(query: String, limit: Int) = withContext(Dispatchers.IO) {
        launchPadDao.forQuery(query, limit)
    }

    private suspend fun getAllLaunchPadsFromApi() = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchPadService.getAllLaunchPads().await()
        }
    }
}