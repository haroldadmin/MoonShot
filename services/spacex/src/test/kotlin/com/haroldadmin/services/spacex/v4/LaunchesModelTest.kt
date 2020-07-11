package com.haroldadmin.services.spacex.v4

import com.squareup.moshi.Moshi
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class LaunchesModelTest: AnnotationSpec() {

    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        moshi = Moshi.Builder()
            .add(ZonedDateTimeAdapter())
            .build()
    }

    @Test
    fun testLaunchModel() {
        val launchJson = this::class.getResource("/sampleData/v4/one_launch.json").readText()
        val jsonAdapter = moshi.adapter(Launch::class.java)
        val launch = jsonAdapter.fromJson(launchJson)

        with(launch) {
            this.shouldNotBeNull()
            id.shouldBe("5eb87d42ffd86e000604b384")
            fairings.shouldBeNull()

            with(links) {
                this.shouldNotBeNull()
                patch.shouldNotBeNull()
                patch?.small shouldBe "https://images2.imgbox.com/53/22/dh0XSLXO_o.png"
                patch?.large shouldBe "https://images2.imgbox.com/15/2b/NAcsTEB6_o.png"

                reddit.shouldNotBeNull()
                reddit?.campaign shouldBe "https://www.reddit.com/r/spacex/comments/ezn6n0/crs20_launch_campaign_thread"
                reddit?.launch shouldBe "https://www.reddit.com/r/spacex/comments/fe8pcj/rspacex_crs20_official_launch_discussion_updates/"
                reddit?.media shouldBe "https://www.reddit.com/r/spacex/comments/fes64p/rspacex_crs20_media_thread_videos_images_gifs/"
            }

            launchDateUTC.toInstant()
                .epochSecond
                .shouldBe(launchDateUnix)
        }
    }
}
