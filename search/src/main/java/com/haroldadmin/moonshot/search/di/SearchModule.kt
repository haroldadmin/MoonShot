package com.haroldadmin.moonshot.search.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_SearchModule::class])
interface SearchModule