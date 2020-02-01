package com.haroldadmin.moonshot.core

import io.kotlintest.matchers.types.shouldNotBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec

class ResourceTest : DescribeSpec({

        describe("Resource") {

            context("Success") {
                val data = Any()
                val successfulResource = Resource.Success(data)

                it("Should contain the same data as was used to create it") {
                    successfulResource.data shouldBe data
                }

                it("Should not be Resource.Error or Resource.Loading") {
                    successfulResource.shouldNotBeTypeOf<Resource.Error<*, *>>()
                    successfulResource.shouldNotBeTypeOf<Resource.Loading>()
                }
            }

            context("Error") {
                val data: Any? = null
                val error = Exception()
                val errorResource = Resource.Error(data, error)

                it("Should have data and exception which were used to create it") {
                    errorResource.data shouldBe data
                    errorResource.error shouldBe error
                }

                it("Should not be Resource.Success or Resource.Loading") {
                    errorResource.shouldNotBeTypeOf<Resource.Success<*>>()
                    errorResource.shouldNotBeTypeOf<Resource.Loading>()
                }
            }

            context("Loading") {
                val loadingResource = Resource.Loading

                it("Should not be Resource.Success or Resource.Error") {
                    loadingResource.shouldNotBeTypeOf<Resource.Success<*>>()
                    loadingResource.shouldNotBeTypeOf<Resource.Error<*, *>>()
                }
            }

            context("Completion property") {
                val data = Any()

                it("Should not be complete if data is cached and resource is Success") {
                    val resource = Resource.Success(data, isCached = true)
                    resource.isComplete shouldBe false
                }

                it("Should be complete if the data is not cached and resource is Success") {
                    val resource = Resource.Success(data, isCached = false)
                    resource.isComplete shouldBe true
                }

                it("Should be complete if the resource is Error") {
                    val resource = Resource.Error(null, null)
                    resource.isComplete shouldBe true
                }

                it ("Should not be complete if the resource is Uninitialized") {
                    val resource = Resource.Uninitialized
                    resource.isComplete shouldBe false
                }

                it ("Should not be complete if the resource is Loading") {
                    val resource = Resource.Loading
                    resource.isComplete shouldBe false
                }
            }
        }
})