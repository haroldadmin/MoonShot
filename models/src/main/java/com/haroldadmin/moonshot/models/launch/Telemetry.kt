package com.haroldadmin.moonshot.models.launch

data class Telemetry(
    val flightClub: String?
) {
    companion object {
        fun getSampleTelemetry() =
            Telemetry("https://www.flightclub.io/results/?code=TS19V")
    }
}