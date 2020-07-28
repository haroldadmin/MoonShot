package com.haroldadmin.moonshot.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.services.spacex.v4.test.useJSON
import com.haroldadmin.moonshot.services.spacex.v4.test.useJSONAdapter
import com.haroldadmin.moonshot.services.spacex.v4.test.useMockService
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.runBlocking

internal class LaunchesTest: AnnotationSpec() {

    @Test
    fun testLaunchModel() {
        val launchJson =
            useJSON("/sampleData/v4/one_launch.json")
        val jsonAdapter =
            useJSONAdapter<Launch>()
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

    @Test
    fun testAllLaunchesResponse() {
        val (service, cleanup) = useMockService<LaunchesService> {
            setBody(com.haroldadmin.moonshot.services.spacex.v4.test.useJSON("/sampleData/v4/all_launches.json"))
        }

        val response = runBlocking { service.all() }
        response.shouldBeInstanceOf<NetworkResponse.Success<List<Launch>>>()
        response as NetworkResponse.Success

        response.body shouldHaveSize 1

        cleanup()
    }
}
