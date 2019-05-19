package com.haroldadmin.moonshot.models.launch.rocket

data class Fairings(
    val reused: Boolean,
    val recoveryAttempt: Boolean,
    val recovered: Boolean,
    val ship: String?
) {
    companion object {
        fun getSampleFairing() = Fairings(
            false,
            false,
            false,
            null
        )
    }
}