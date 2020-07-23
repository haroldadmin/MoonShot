package com.haroldadmin.moonshot.features.launches.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_LaunchesModule::class])
interface LaunchesModule
