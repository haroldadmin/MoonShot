package com.haroldadmin.moonshot.search.di

import com.haroldadmin.moonshot.base.di.FeatureScope
import com.haroldadmin.moonshot.base.di.MoonShotFragmentComponent
import com.haroldadmin.moonshot.di.AppComponent
import com.haroldadmin.moonshot.search.SearchFragment
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@Component(modules = [SearchModule::class], dependencies = [AppComponent::class])
@FeatureScope
interface SearchComponent : MoonShotFragmentComponent<SearchFragment>