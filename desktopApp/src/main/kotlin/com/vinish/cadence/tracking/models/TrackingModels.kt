package com.vinish.cadence.tracking.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Duration
import java.time.Instant

object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("java.time.Instant", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Instant) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Instant = Instant.parse(decoder.decodeString())
}

@Serializable
data class TrackingEvent(
    val appName: String,
    val windowTitle: String,
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant
)

@Serializable
data class Segment(
    val appName: String,
    val windowTitle: String,
    @Serializable(with = InstantSerializer::class)
    val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    var endTime: Instant? = null
) {
    val durationSeconds: Long
        get() {
            val end = endTime ?: Instant.now()
            return Duration.between(startTime, end).seconds
        }
}

@Serializable
data class Session(
    @Serializable(with = InstantSerializer::class)
    val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    var endTime: Instant? = null,
    val segments: MutableList<Segment> = mutableListOf()
) {
    val durationSeconds: Long
        get() {
            val end = endTime ?: Instant.now()
            return Duration.between(startTime, end).seconds
        }
}
