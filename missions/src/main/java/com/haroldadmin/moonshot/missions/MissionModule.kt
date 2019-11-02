package com.haroldadmin.moonshot.missions

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_MissionModule::class])
interface MissionModule