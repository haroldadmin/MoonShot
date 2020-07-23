package com.haroldadmin.moonshot.features.launches

import com.haroldadmin.moonshot.base.di.V4API
import com.haroldadmin.moonshot.db.Launch
import com.haroldadmin.moonshot.db.LaunchQueries
import com.haroldadmin.moonshot.services.spacex.v4.LaunchesService
import javax.inject.Inject

class AllLaunchesUsecase @Inject constructor(
    private val launchQueries: LaunchQueries,
    @V4API private val launchesService: LaunchesService
) {

}