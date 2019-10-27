package com.haroldadmin.moonshotRepository.applicationInfo

import com.haroldadmin.moonshot.database.ApplicationInfoDao
import com.haroldadmin.moonshot.models.ApplicationInfo
import java.util.Date

internal class FakeApplicationInfoDao : ApplicationInfoDao {

    private var applicationInfo: ApplicationInfo =
        ApplicationInfo(Date().time)

    override suspend fun applicationInfo(): ApplicationInfo? {
        return applicationInfo
    }

    override suspend fun save(info: ApplicationInfo) {
        applicationInfo = info
    }

    override suspend fun update(info: ApplicationInfo) {
        applicationInfo = info
    }
}