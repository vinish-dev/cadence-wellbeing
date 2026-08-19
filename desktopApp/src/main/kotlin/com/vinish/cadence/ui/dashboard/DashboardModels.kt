package com.vinish.cadence.ui.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.vinish.cadence.tracking.AppUsage
import com.vinish.cadence.tracking.TrackerState
import com.vinish.cadence.ui.theme.CadenceBlue
import com.vinish.cadence.ui.theme.CadenceBlueSoft
import com.vinish.cadence.ui.theme.CadenceGraySoft
import com.vinish.cadence.ui.theme.CadenceGreen
import com.vinish.cadence.ui.theme.CadenceGreenSoft
import com.vinish.cadence.ui.theme.CadenceIdle
import com.vinish.cadence.ui.theme.CadenceOrange

import com.vinish.cadence.ui.theme.CadencePurple
import com.vinish.cadence.ui.theme.CadencePurpleSoft
import com.vinish.cadence.ui.theme.CadenceOrangeSoft
import com.vinish.cadence.ui.theme.CadenceBlueSoft
import java.time.LocalDate
import kotlin.math.absoluteValue
import java.time.format.DateTimeFormatter
import java.time.Instant
import java.util.Locale
import kotlin.math.roundToInt
import com.vinish.cadence.tracking.models.Session
import com.vinish.cadence.tracking.models.Segment
import java.time.ZoneId
import com.vinish.cadence.ui.activity.SessionSummaryData
import com.vinish.cadence.ui.activity.TimelineSegmentData
import kotlin.math.absoluteValue

data class MetricCardData(
    val title: String,
    val value: String,
    val trend: String,
    val trendPositive: Boolean,
    val caption: String,
    val icon: ImageVector,
    val iconTint: Color,
    val iconBackground: Color,
)

data class FocusDetail(
    val value: String,
    val label: String,
    val color: Color,
)

data class AppUsageData(
    val name: String,
    val duration: String,
    val shareLabel: String,
    val share: Float,
    val color: Color,
)

data class ChartPointSet(
    val label: String,
    val color: Color,
    val values: List<Float>,
)

data class BreakInfoData(
    val currentFocusMinutes: Int,
    val nextBreakMinutes: Int,
    val isRecommendationActive: Boolean,
    val isSnoozed: Boolean
)

data class TrackingStatusData(
    val isActive: Boolean,
    val startedAt: String,
    val trackedToday: String,
    val actionLabel: String,
)

data class DashboardUiState(
    val greetingName: String,
    val todayLabel: String,
    val activeAppName: String,
    val activeWindowTitle: String,
    val activeSessionDuration: String,
    val metricCards: List<MetricCardData>,
    val activitySeries: List<ChartPointSet>,
    val currentFocusDetails: List<FocusDetail>,
    val appUsage: List<AppUsageData>,
    val totalFocusedTime: String,
    val timeline: List<TimelineSegmentData>,
    val breakInfo: BreakInfoData,
    val trackingStatus: TrackingStatusData,
    val sessions: List<SessionSummaryData>,
)

fun mockDashboardState(): DashboardUiState = DashboardUiState(
    greetingName = "Vinish",
    todayLabel = "May 18, 2025",
    activeAppName = "Brave Browser",
    activeWindowTitle = "Kotlin Docs - Coroutines Guide",
    activeSessionDuration = "01:24:17",
    metricCards = listOf(
        MetricCardData(
            title = "Keys Typed",
            value = "12,483",
            trend = "18.6%",
            trendPositive = true,
            caption = "vs yesterday",
            icon = Icons.Outlined.Keyboard,
            iconTint = CadencePurple,
            iconBackground = CadencePurpleSoft,
        ),
        MetricCardData(
            title = "Focused Time",
            value = "4h 22m",
            trend = "12.4%",
            trendPositive = true,
            caption = "vs yesterday",
            icon = Icons.Outlined.Schedule,
            iconTint = CadenceGreen,
            iconBackground = CadenceGreenSoft,
        ),
        MetricCardData(
            title = "Apps Used",
            value = "9",
            trend = "",
            trendPositive = true,
            caption = "vs yesterday",
            icon = Icons.Outlined.Apps,
            iconTint = CadenceBlue,
            iconBackground = CadenceBlueSoft,
        ),
        MetricCardData(
            title = "Focus Score",
            value = "78%",
            trend = "8%",
            trendPositive = true,
            caption = "vs yesterday",
            icon = Icons.Outlined.RadioButtonChecked,
            iconTint = CadencePurple,
            iconBackground = CadencePurpleSoft,
        ),
    ),
    activitySeries = listOf(
        ChartPointSet(
            label = "Typing",
            color = CadencePurple,
            values = listOf(0.05f, 0.22f, 0.18f, 0.41f, 0.57f, 0.83f, 0.79f, 0.62f, 0.38f, 0.56f, 0.44f, 0.49f, 0.86f, 0.33f, 0.59f, 0.81f, 0.42f, 0.67f, 0.74f, 0.96f, 0.55f, 0.24f, 0.11f, 0.19f),
        ),
        ChartPointSet(
            label = "Mouse",
            color = CadenceGreen,
            values = listOf(0.12f, 0.41f, 0.29f, 0.35f, 0.13f, 0.18f, 0.27f, 0.22f, 0.64f, 0.34f, 0.58f, 0.27f, 0.49f, 0.26f, 0.31f, 0.38f, 0.24f, 0.29f, 0.35f, 0.47f, 0.19f, 0.14f, 0.09f, 0.07f),
        ),
        ChartPointSet(
            label = "Idle",
            color = CadenceIdle,
            values = listOf(0.02f, 0.04f, 0.09f, 0.08f, 0.12f, 0.18f, 0.23f, 0.17f, 0.05f, 0.47f, 0.18f, 0.12f, 0.06f, 0.03f, 0.04f, 0.08f, 0.14f, 0.12f, 0.19f, 0.22f, 0.18f, 0.11f, 0.06f, 0.02f),
        ),
    ),
    currentFocusDetails = listOf(
        FocusDetail("890", "Keys typed", CadencePurple),
        FocusDetail("14m", "Idle time", CadenceGreen),
        FocusDetail("105", "Clicks", CadenceBlue),
    ),
    appUsage = listOf(
        AppUsageData("IntelliJ IDEA", "2h 18m", "41%", 0.41f, CadencePurple),
        AppUsageData("Brave Browser", "1h 12m", "22%", 0.22f, Color(0xFFFF7A1A)),
        AppUsageData("VS Code", "43m", "13%", 0.13f, CadenceBlue),
        AppUsageData("Notepad", "26m", "8%", 0.08f, CadenceOrange),
        AppUsageData("Spotify", "21m", "7%", 0.07f, CadenceGreen),
        AppUsageData("Others", "14m", "5%", 0.05f, CadenceGraySoft),
    ),
    totalFocusedTime = "4h 22m",
    timeline = listOf(
        TimelineSegmentData("IntelliJ IDEA", "9:00 AM", 2.6f, CadencePurple),
        TimelineSegmentData("Brave", "9:45 AM", 1.2f, CadenceGreen),
        TimelineSegmentData("IntelliJ IDEA", "10:10 AM", 2.4f, Color(0xFF7C68FF)),
        TimelineSegmentData("Notepad", "11:20 AM", 1.1f, CadenceOrange),
        TimelineSegmentData("Brave", "11:35 AM", 1.4f, Color(0xFF5BD2AF)),
        TimelineSegmentData("", "12:30 PM", 1.8f, CadenceGraySoft),
    ),
    breakInfo = BreakInfoData(
        currentFocusMinutes = 42,
        nextBreakMinutes = 3,
        isRecommendationActive = false,
        isSnoozed = false
    ),
    trackingStatus = TrackingStatusData(
        isActive = true,
        startedAt = "9:01 AM",
        trackedToday = "5h 21m",
        actionLabel = "Pause tracking",
    ),
    sessions = listOf(
        SessionSummaryData(
            name = "Session 2",
            timeRange = "4:43 PM – 5:12 PM",
            duration = "29 min",
            timeline = listOf(
                TimelineSegmentData("IntelliJ IDEA", "4:43 PM", 1.8f, CadencePurple),
                TimelineSegmentData("Brave", "5:01 PM", 0.6f, CadenceGreen),
                TimelineSegmentData("Notepad", "5:08 PM", 0.4f, CadenceOrange)
            ),
            apps = listOf(
                AppUsageData("IntelliJ IDEA", "18m", "62%", 0.62f, CadencePurple),
                AppUsageData("Brave Browser", "6m", "21%", 0.21f, CadenceGreen),
                AppUsageData("Notepad", "4m", "14%", 0.14f, CadenceOrange)
            )
        ),
        SessionSummaryData(
            name = "Session 1",
            timeRange = "4:07 PM – 4:35 PM",
            duration = "28 min",
            timeline = listOf(
                TimelineSegmentData("IntelliJ IDEA", "4:07 PM", 2.0f, CadencePurple),
                TimelineSegmentData("VS Code", "4:27 PM", 0.8f, CadenceBlue)
            ),
            apps = listOf(
                AppUsageData("IntelliJ IDEA", "20m", "71%", 0.71f, CadencePurple),
                AppUsageData("VS Code", "8m", "29%", 0.29f, CadenceBlue)
            )
        )
    )
)

fun dashboardStateFromTracking(
    typingCount: Int,
    clickCount: Int,
    trackerState: TrackerState,
    currentSession: Session? = null,
    pastSessions: List<Session> = emptyList(),
    focusState: com.vinish.cadence.tracking.FocusState = com.vinish.cadence.tracking.FocusState(),
): DashboardUiState {
    val pastAppUsages = mutableMapOf<String, Long>()
    pastSessions.forEach { session ->
        session.segments.forEach { seg ->
            pastAppUsages[seg.appName] = pastAppUsages.getOrDefault(seg.appName, 0L) + seg.durationSeconds
        }
    }
    
    val combinedUsages = pastAppUsages.toMutableMap()
    trackerState.appUsages.forEach { usage ->
        combinedUsages[usage.appName] = combinedUsages.getOrDefault(usage.appName, 0L) + usage.durationSeconds
    }
    
    val totalTrackedSeconds = combinedUsages.values.sum()
    val activeSessionSeconds = currentSession?.durationSeconds ?: 0L
    
    val currentFocusMinutes = focusState.currentFocusMinutes
    val isSnoozed = focusState.snoozeUntil != null && java.time.Instant.now().isBefore(focusState.snoozeUntil)
    val threshold = com.vinish.cadence.tracking.SettingsManager.settings.value.breakTimerMinutes
    val isRecommendationActive = currentFocusMinutes >= threshold && !isSnoozed
    val nextBreakMinutes = if (isSnoozed && focusState.snoozeUntil != null) {
        java.time.Duration.between(java.time.Instant.now(), focusState.snoozeUntil).toMinutes().toInt().coerceAtLeast(0)
    } else {
        (threshold - currentFocusMinutes).coerceAtLeast(0)
    }

    val activeAppName = trackerState.activeApp.takeUnless { it == "None" } ?: "No active app"
    val activeWindowTitle = trackerState.activeWindowTitle.ifBlank { "Waiting for app activity" }

    val formatterTime = java.time.format.DateTimeFormatter.ofPattern("h:mm a").withZone(java.time.ZoneId.systemDefault())
    
    val mappedSessions = pastSessions.mapIndexed { index, session ->
        val sessionNum = pastSessions.size - index
        val startStr = formatterTime.format(session.startTime)
        val endStr = session.endTime?.let { formatterTime.format(it) } ?: "Now"
        val durationMin = (session.durationSeconds / 60).toInt()
        
        // Aggregate apps for this session
        val appDurations = mutableMapOf<String, Long>()
        session.segments.forEach { seg ->
            appDurations[seg.appName] = appDurations.getOrDefault(seg.appName, 0L) + seg.durationSeconds
        }
        val totalSessionDuration = session.durationSeconds.coerceAtLeast(1L)
        val sortedApps = appDurations.entries.sortedByDescending { it.value }.take(4)
        
        val apps = sortedApps.map { (appName, duration) ->
            val share = duration.toFloat() / totalSessionDuration.toFloat()
            val percentage = (share * 100).toInt().toString() + "%"
            val color = getAppColor(appName)
            AppUsageData(appName, formatCompactDuration(duration), percentage, share, color)
        }
        
        // Convert segments to timeline
        val timeline = session.segments.toTimelineSegments()
        
        SessionSummaryData(
            name = "Session $sessionNum",
            timeRange = "$startStr – $endStr",
            duration = "$durationMin min",
            timeline = timeline,
            apps = apps
        )
    }

    return DashboardUiState(
        greetingName = com.vinish.cadence.tracking.SettingsManager.settings.value.userName,
        todayLabel = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)),
        activeAppName = activeAppName,
        activeWindowTitle = activeWindowTitle,
        activeSessionDuration = formatClockDuration(activeSessionSeconds),
        metricCards = listOf(
            MetricCardData(
                title = "Keys Typed",
                value = typingCount.formatWithGrouping(),
                trend = "",
                trendPositive = true,
                caption = "Live today",
                icon = Icons.Outlined.Keyboard,
                iconTint = CadencePurple,
                iconBackground = CadencePurpleSoft,
            ),
            MetricCardData(
                title = "Focused Time",
                value = formatClockDuration(totalTrackedSeconds),
                trend = "",
                trendPositive = true,
                caption = "Live today",
                icon = Icons.Outlined.Timelapse,
                iconTint = CadenceGreen,
                iconBackground = CadenceGreenSoft,
            ),
            MetricCardData(
                title = "Apps Used",
                value = combinedUsages.size.toString(),
                trend = "",
                trendPositive = false,
                caption = "Live today",
                icon = Icons.Outlined.Apps,
                iconTint = CadenceOrange,
                iconBackground = CadenceOrangeSoft,
            ),
            MetricCardData(
                title = "Mouse Clicks",
                value = clickCount.formatWithGrouping(),
                trend = "",
                trendPositive = true,
                caption = "Live today",
                icon = Icons.Outlined.AutoGraph,
                iconTint = CadenceBlue,
                iconBackground = CadenceBlueSoft,
            )
        ),
        activitySeries = mockDashboardState().activitySeries,
        currentFocusDetails = listOf(
            FocusDetail(typingCount.toString(), "Keys typed", CadencePurple),
            FocusDetail("${nextBreakMinutes}m", "Until break", CadenceGreen),
        ),
        appUsage = combinedUsages.entries
            .sortedByDescending { it.value }
            .map { (appName, durationSeconds) ->
                val total = totalTrackedSeconds.coerceAtLeast(1L)
                val share = (durationSeconds.toFloat() / total.toFloat()).coerceAtLeast(0.01f)
                val percentage = (share * 100).toInt().toString() + "%"
                val color = getAppColor(appName)
                AppUsageData(appName, formatCompactDuration(durationSeconds), percentage, share, color)
            },
        totalFocusedTime = formatClockDuration(totalTrackedSeconds),
        timeline = (currentSession?.segments ?: emptyList()).toTimelineSegments(),
        breakInfo = BreakInfoData(
            currentFocusMinutes = currentFocusMinutes,
            nextBreakMinutes = nextBreakMinutes,
            isRecommendationActive = isRecommendationActive,
            isSnoozed = isSnoozed
        ),
        trackingStatus = TrackingStatusData(
            isActive = true,
            startedAt = "Now",
            trackedToday = formatCompactDuration(totalTrackedSeconds),
            actionLabel = "Tracking live",
        ),
        sessions = mappedSessions,
    )
}

fun getAppColor(appName: String): Color {
    val palette = listOf(
        CadencePurple,
        Color(0xFFFF7A1A), // Bright orange
        CadenceBlue,
        CadenceOrange,
        CadenceGreen,
        Color(0xFF7C68FF),
        Color(0xFF5BD2AF),
    )
    
    return when (appName.lowercase(Locale.ENGLISH)) {
        "intellij idea", "studio64", "idea64" -> CadencePurple
        "brave browser", "chrome", "firefox", "msedge" -> CadenceGreen
        "code", "vs code" -> CadenceBlue
        "notepad" -> CadenceOrange
        else -> {
            // Pick a consistent vibrant color from the palette based on the app name hash
            val hash = appName.hashCode().absoluteValue
            palette[hash % palette.size]
        }
    }
}

private fun List<AppUsage>.toDashboardAppUsage(): List<AppUsageData> {
    if (isEmpty()) {
        return listOf(
            AppUsageData(
                name = "Waiting for activity",
                duration = "0m",
                shareLabel = "0%",
                share = 0f,
                color = CadenceGraySoft,
            ),
        )
    }

    val totalSeconds = sumOf(AppUsage::durationSeconds).coerceAtLeast(1L)
    val palette = listOf(
        CadencePurple,
        Color(0xFFFF7A1A),
        CadenceBlue,
        CadenceOrange,
        CadenceGreen,
        CadenceGraySoft,
    )

    return mapIndexed { index, usage ->
        val share = usage.durationSeconds.toFloat() / totalSeconds.toFloat()
        AppUsageData(
            name = usage.appName,
            duration = usage.formattedDuration,
            shareLabel = "${(share * 100).roundToInt()}%",
            share = share,
            color = palette[index % palette.size],
        )
    }
}

private fun List<Segment>.toTimelineSegments(): List<TimelineSegmentData> {
    if (isEmpty()) {
        return listOf(
            TimelineSegmentData(
                label = "",
                startTime = "No activity yet",
                weight = 1f,
                color = CadenceGraySoft,
            ),
        )
    }
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a").withZone(ZoneId.systemDefault())

    data class MergedSegment(
        val appName: String,
        val startTime: Instant,
        var durationSeconds: Long
    )

    val merged = mutableListOf<MergedSegment>()
    for (segment in this) {
        val duration = segment.durationSeconds
        if (merged.isEmpty()) {
            merged.add(MergedSegment(segment.appName, segment.startTime, duration))
        } else {
            val last = merged.last()
            if (last.appName == segment.appName) {
                last.durationSeconds += duration
            } else if (duration < 60) {
                // Short app switch. Merge its duration into the previous app to prevent timeline clutter.
                last.durationSeconds += duration
            } else {
                merged.add(MergedSegment(segment.appName, segment.startTime, duration))
            }
        }
    }

    return merged.map { segment ->
        TimelineSegmentData(
            label = segment.appName,
            startTime = timeFormatter.format(segment.startTime),
            weight = segment.durationSeconds.coerceAtLeast(1L).toFloat(),
            color = getAppColor(segment.appName),
        )
    }
}

private fun Int.formatWithGrouping(): String = "%,d".format(Locale.ENGLISH, this)

fun formatCompactDuration(seconds: Long): String {
    if (seconds <= 0L) return "0m"

    val totalMinutes = seconds / 60
    if (totalMinutes < 60) return "${totalMinutes}m"

    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return if (minutes == 0L) "${hours}h" else "${hours}h ${minutes}m"
}

private fun formatClockDuration(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d:%02d".format(Locale.ENGLISH, hours, minutes, remainingSeconds)
}

private fun focusScore(typingCount: Int, totalTrackedSeconds: Long): String {
    if (totalTrackedSeconds <= 0L) return "0%"

    val score = ((typingCount / totalTrackedSeconds.toFloat()) * 60f)
        .roundToInt()
        .coerceIn(0, 100)
    return "$score%"
}
