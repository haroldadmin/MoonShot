package com.haroldadmin.moonshot.database.launch.rocket

data class Fairings(
    val reused: Boolean,
    val recoveryAttempt: Boolean,
    val recovered: Boolean,
    val ship: String?
) {
    companion object {
        internal fun getSampleFairing() = Fairings(
            false,
            false,
            false,
            null
        )
    }
}