package com.haroldadmin.moonshot

import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.core.ImmediateDispatchers
import com.haroldadmin.moonshot.core.TestDispatchers
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(modules = [
    TestDispatchersModule::class
])
internal interface TestComponent {

    fun inject(notifierTest: NotifierTest)
}

@Module
interface TestDispatchersModule {
    @Binds
    fun testDispatchers(testDispatchers: ImmediateDispatchers): AppDispatchers
}
