package com.haroldadmin.moonshot.launchDetails.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_LaunchDetailsModule::class])
interface LaunchDetailsModule
