package com.haroldadmin.moonshot.launches.di

import com.haroldadmin.moonshot.base.di.FeatureScope
import com.haroldadmin.moonshot.base.di.MoonShotFragmentComponent
import com.haroldadmin.moonshot.di.AppComponent
import com.haroldadmin.moonshot.launches.LaunchesFragment
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@FeatureScope
@Component(modules = [LaunchesModule::class], dependencies = [AppComponent::class])
interface LaunchesComponent : MoonShotFragmentComponent<LaunchesFragment>