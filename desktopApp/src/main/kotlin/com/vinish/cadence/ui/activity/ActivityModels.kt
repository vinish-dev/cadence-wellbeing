package com.vinish.cadence.ui.activity

import androidx.compose.ui.graphics.Color
import com.vinish.cadence.ui.dashboard.AppUsageData

data class TimelineSegmentData(
    val label: String,
    val startTime: String,
    val weight: Float,
    val color: Color,
)

data class SessionSummaryData(
    val name: String,
    val timeRange: String,
    val duration: String,
    val timeline: List<TimelineSegmentData>,
    val apps: List<AppUsageData>,
)
