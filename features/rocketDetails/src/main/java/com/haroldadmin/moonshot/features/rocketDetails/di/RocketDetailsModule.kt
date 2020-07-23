package com.haroldadmin.moonshot.features.rocketDetails.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_RocketDetailsModule::class])
interface RocketDetailsModule
