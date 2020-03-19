package com.haroldadmin.moonshot.settings

import android.content.Context
import com.haroldadmin.moonshot.base.di.FeatureScope
import com.haroldadmin.moonshot.base.di.MoonShotFragmentComponent
import com.haroldadmin.moonshot.di.AppComponent
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [AppComponent::class])
@FeatureScope
interface SettingsComponent : MoonShotFragmentComponent<SettingsFragment> {
    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent,
            @BindsInstance context: Context
        ): SettingsComponent
    }
}