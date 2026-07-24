package com.vinish.cadence.tracking.models

import java.time.Duration
import java.time.Instant

data class TrackingEvent(
    val appName: String,
    val windowTitle: String,
    val timestamp: Instant
)

data class Segment(
    val appName: String,
    val windowTitle: String,
    val startTime: Instant,
    var endTime: Instant? = null
) {
    val durationSeconds: Long
        get() {
            val end = endTime ?: Instant.now()
            return Duration.between(startTime, end).seconds
        }
}

data class Session(
    val startTime: Instant,
    var endTime: Instant? = null,
    val segments: MutableList<Segment> = mutableListOf()
) {
    val durationSeconds: Long
        get() {
            val end = endTime ?: Instant.now()
            return Duration.between(startTime, end).seconds
        }
}
