package com.haroldadmin.moonshot.database.launch

data class Telemetry(
    val flightClub: String
) {
    companion object {
        internal fun getSampleTelemetry() = Telemetry("https://www.flightclub.io/results/?code=TS19V")
    }
}