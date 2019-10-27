package com.haroldadmin.moonshot.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

internal abstract class DaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val dbRule = MoonShotDbRule()
}
