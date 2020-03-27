package com.haroldadmin.moonshot

import dagger.Component

@Component
internal interface TestComponent {

    fun inject(notifierTest: NotifierTest)

}
