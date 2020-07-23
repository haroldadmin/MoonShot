package com.haroldadmin.moonshot.features.rockets.di

import com.haroldadmin.moonshot.base.di.FeatureScope
import com.haroldadmin.moonshot.base.di.MoonShotFragmentComponent
import com.haroldadmin.moonshot.di.AppComponent
import com.haroldadmin.moonshot.features.rockets.RocketsFragment
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@FeatureScope
@Component(modules = [RocketsModule::class], dependencies = [AppComponent::class])
interface RocketsComponent : MoonShotFragmentComponent<RocketsFragment>