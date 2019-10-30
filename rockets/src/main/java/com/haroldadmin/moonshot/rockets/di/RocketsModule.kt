package com.haroldadmin.moonshot.rockets.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_RocketsModule::class])
interface RocketsModule
