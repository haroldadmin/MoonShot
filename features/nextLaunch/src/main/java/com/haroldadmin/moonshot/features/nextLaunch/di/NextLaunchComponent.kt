package com.haroldadmin.moonshot.features.nextLaunch.di

import com.haroldadmin.moonshot.di.AppComponent
import com.haroldadmin.moonshot.base.di.FeatureScope
import com.haroldadmin.moonshot.base.di.MoonShotFragmentComponent
import com.haroldadmin.moonshot.features.nextLaunch.NextLaunchFragment
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@FeatureScope
@Component(modules = [NextLaunchModule::class], dependencies = [AppComponent::class])
interface NextLaunchComponent : MoonShotFragmentComponent<NextLaunchFragment>
