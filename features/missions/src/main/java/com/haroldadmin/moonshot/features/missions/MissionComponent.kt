package com.haroldadmin.moonshot.features.missions

import com.haroldadmin.moonshot.base.di.FeatureScope
import com.haroldadmin.moonshot.base.di.MoonShotFragmentComponent
import com.haroldadmin.moonshot.di.AppComponent
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Component(modules = [MissionModule::class], dependencies = [AppComponent::class])
@FeatureScope
interface MissionComponent : MoonShotFragmentComponent<MissionFragment>
