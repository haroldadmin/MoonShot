package com.haroldadmin.moonshot.core

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.DescribeSpec


class ResourceTest: DescribeSpec({

    describe("Resource") {

        context("Success") {
            val data = Any()
            val successfulResource = Resource.Success<Any>(data)

            it ("Should contain the same data as was used to create it") {
                successfulResource.data shouldBe data
            }

            it("Should not be Resource.Error or Resource.Loading") {
                (successfulResource is Resource.Error<*, *>) shouldNotBe true
                (successfulResource is Resource.Loading<*>) shouldNotBe true
            }
        }

        context("Error") {
            val data: Any? = null
            val error = Exception()
            val errorResource = Resource.Error(data, error)

            it ("Should have data and exception which were used to create it") {
                errorResource.data shouldBe data
                errorResource.error shouldBe error
            }

            it("Should not be Resource.Success or Resource.Loading") {
                (errorResource is Resource.Success<*>) shouldNotBe true
                (errorResource is Resource.Loading<*>) shouldNotBe true
            }
        }

        context("Loading") {
            val loadingResource = Resource.Loading<Any>()

            it ("Should not be Resource.Success or Resource.Error") {
                (loadingResource is Resource.Success<*>) shouldNotBe true
                (loadingResource is Resource.Error<*, *>) shouldNotBe true
            }
        }

    }

})