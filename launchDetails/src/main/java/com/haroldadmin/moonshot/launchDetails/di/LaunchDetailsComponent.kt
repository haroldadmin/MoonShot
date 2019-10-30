package com.haroldadmin.moonshot.launchDetails.di

import com.haroldadmin.moonshot.di.AppComponent
import com.haroldadmin.moonshot.base.di.FeatureScope
import com.haroldadmin.moonshot.base.di.MoonShotFragmentComponent
import com.haroldadmin.moonshot.launchDetails.LaunchDetailsFragment
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@FeatureScope
@Component(modules = [LaunchDetailsModule::class], dependencies = [AppComponent::class])
interface LaunchDetailsComponent : MoonShotFragmentComponent<LaunchDetailsFragment>
