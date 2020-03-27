package com.haroldadmin.moonshotRepository.applicationInfo

import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.database.ApplicationInfoDao
import com.haroldadmin.moonshot.models.ApplicationInfo
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApplicationInfoUseCase @Inject constructor(
    private val dao: ApplicationInfoDao,
    private val appDispatchers: AppDispatchers
) {

    suspend fun getApplicationInfo(): ApplicationInfo? {
        return dao.applicationInfo()
    }

    suspend fun update(applicationInfo: ApplicationInfo) = withContext(appDispatchers.IO) {
        dao.update(applicationInfo)
    }

    suspend fun save(applicationInfo: ApplicationInfo) = withContext(appDispatchers.IO) {
        dao.save(applicationInfo)
    }
}