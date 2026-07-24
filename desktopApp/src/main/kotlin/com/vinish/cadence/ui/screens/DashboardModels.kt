package com.vinish.cadence.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.vinish.cadence.ui.theme.CadenceBlue
import com.vinish.cadence.ui.theme.CadenceBlueSoft
import com.vinish.cadence.ui.theme.CadenceGraySoft
import com.vinish.cadence.ui.theme.CadenceGreen
import com.vinish.cadence.ui.theme.CadenceGreenSoft
import com.vinish.cadence.ui.theme.CadenceIdle
import com.vinish.cadence.ui.theme.CadenceOrange
import com.vinish.cadence.ui.theme.CadenceOrangeSoft
import com.vinish.cadence.ui.theme.CadencePurple
import com.vinish.cadence.ui.theme.CadencePurpleSoft

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

data class TimelineSegmentData(
    val label: String,
    val startTime: String,
    val weight: Float,
    val color: Color,
)

data class BreakInfoData(
    val currentFocusMinutes: Int,
    val nextBreakMinutes: Int,
)

data class TrackingStatusData(
    val startedAt: String,
    val trackedToday: String,
    val actionLabel: String,
)

data class DashboardUiState(
    val greetingName: String,
    val todayLabel: String,
    val metricCards: List<MetricCardData>,
    val activitySeries: List<ChartPointSet>,
    val currentFocusDetails: List<FocusDetail>,
    val appUsage: List<AppUsageData>,
    val timeline: List<TimelineSegmentData>,
    val breakInfo: BreakInfoData,
    val trackingStatus: TrackingStatusData,
)

fun mockDashboardState(): DashboardUiState = DashboardUiState(
    greetingName = "Vinish",
    todayLabel = "May 18, 2025",
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
        nextBreakMinutes = 18,
    ),
    trackingStatus = TrackingStatusData(
        startedAt = "9:01 AM",
        trackedToday = "5h 21m",
        actionLabel = "Pause tracking",
    ),
)
