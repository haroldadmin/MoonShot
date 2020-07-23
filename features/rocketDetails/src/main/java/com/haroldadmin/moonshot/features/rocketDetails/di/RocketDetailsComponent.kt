package com.haroldadmin.moonshot.features.rocketDetails.di

import com.haroldadmin.moonshot.base.di.FeatureScope
import com.haroldadmin.moonshot.base.di.MoonShotFragmentComponent
import com.haroldadmin.moonshot.di.AppComponent
import com.haroldadmin.moonshot.features.rocketDetails.RocketDetailsFragment
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@FeatureScope
@Component(modules = [RocketDetailsModule::class], dependencies = [AppComponent::class])
interface RocketDetailsComponent : MoonShotFragmentComponent<RocketDetailsFragment>