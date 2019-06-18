package com.haroldadmin.moonshot.core

import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec

class ConsumableTest : DescribeSpec({

    describe("Consumable") {


        context("Consume unconsumed data") {

            val consumable = Consumable(Unit)

            it("Should return contained data when consumed") {
                val data = consumable.consume()
                data shouldBe Unit
            }

            it("Should return null when consumed again") {
                val data = consumable()
                data shouldBe null
            }
        }

    }

})