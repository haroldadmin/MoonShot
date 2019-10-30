package com.haroldadmin.moonshot.settings

import com.haroldadmin.moonshot.base.di.FeatureScope
import com.haroldadmin.moonshot.base.di.MoonShotFragmentComponent
import com.haroldadmin.moonshot.di.AppComponent
import dagger.Component

@Component(dependencies = [AppComponent::class])
@FeatureScope
interface SettingsComponent : MoonShotFragmentComponent<SettingsFragment>