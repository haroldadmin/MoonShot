package com.haroldadmin.moonshot.models.info

data class Headquarters(
    val address: String,
    val city: String,
    val state: String
) {

    companion object {
        fun getSampleHeadquarters(): Headquarters {
            return Headquarters(
                address = "Rocket Road",
                city = "Hawthorne",
                state = "California"
            )
        }
    }

}