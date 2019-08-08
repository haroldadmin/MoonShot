package com.haroldadmin.moonshotRepository

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first

@ExperimentalCoroutinesApi
class DbBoundResourceTest : DescribeSpec({
    describe("Emission sequence") {
        context("When db cache is invalid") {
            val dbData: Unit? = null
            val resource = dbBoundResource(
                dbFetcher = { dbData },
                validator = { data -> data != null }
            )

            it("Should emit Resource.Loading first") {
                resource.first().shouldBeTypeOf<Resource.Loading>()
            }

            it("Should emit Resource.Error eventually") {
                val res = resource.last()
                with(res) {
                    shouldBeTypeOf<Resource.Error<*, *>>()
                    this as Resource.Error<*, *>
                    data shouldBe dbData
                }
            }
        }

        context("When db cache is valid") {
            val dbData = Unit
            val resource = dbBoundResource(
                dbFetcher = { dbData },
                validator = { true }
            )

            it("Should emit Resource.Loading first") {
                resource.first().shouldBeTypeOf<Resource.Loading>()
            }

            it("Should emit Resource.Success eventually") {
                val res = resource.last()
                with(res) {
                    shouldBeTypeOf<Resource.Success<Unit>>()
                    this as Resource.Success
                    data shouldBe dbData
                }
            }
        }
    }
})