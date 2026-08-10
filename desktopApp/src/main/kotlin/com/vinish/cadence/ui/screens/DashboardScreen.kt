package com.vinish.cadence.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vinish.cadence.ui.components.ActivityChartCard
import com.vinish.cadence.ui.components.ActivityTimeline
import com.vinish.cadence.ui.components.BreakCard
import com.vinish.cadence.ui.components.CurrentFocusCard
import com.vinish.cadence.ui.components.DashboardCard
import com.vinish.cadence.ui.components.MetricCard
import com.vinish.cadence.ui.components.Sidebar
import com.vinish.cadence.ui.components.TopAppsCard
import com.vinish.cadence.ui.components.TrackingCard
import com.vinish.cadence.ui.navigation.CadenceDestination
import com.vinish.cadence.ui.theme.CadenceBackground
import com.vinish.cadence.ui.theme.CadenceGraySoft
import com.vinish.cadence.ui.theme.CadencePurple
import com.vinish.cadence.ui.theme.CadenceTextPrimary
import com.vinish.cadence.ui.theme.CadenceTextSecondary

private enum class DashboardLayoutMode {
    Compact,
    Medium,
    Expanded,
}

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    selectedDestination: CadenceDestination,
    onDestinationSelected: (CadenceDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = CadenceBackground,
    ) {
        BoxWithConstraints {
            val layoutMode = when {
                maxWidth >= 1320.dp -> DashboardLayoutMode.Expanded
                maxWidth >= 980.dp -> DashboardLayoutMode.Medium
                else -> DashboardLayoutMode.Compact
            }
            val sidebarCompact = layoutMode != DashboardLayoutMode.Expanded

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFF7F8FD), Color(0xFFFFFFFF), Color(0xFFF8FBFF)),
                        ),
                    ),
            ) {
                Sidebar(
                    selectedDestination = selectedDestination,
                    onDestinationSelected = onDestinationSelected,
                    compact = sidebarCompact,
                )
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    when (selectedDestination) {
                        CadenceDestination.Dashboard -> {
                            item {
                                DashboardHeader(
                                    greetingName = state.greetingName,
                                    todayLabel = state.todayLabel,
                                )
                            }
                            item {
                                MetricSection(
                                    metrics = state.metricCards,
                                    layoutMode = layoutMode,
                                )
                            }
                            item {
                                when (layoutMode) {
                                    DashboardLayoutMode.Expanded -> ExpandedContent(state)
                                    DashboardLayoutMode.Medium -> MediumContent(state)
                                    DashboardLayoutMode.Compact -> CompactContent(state)
                                }
                            }
                        }

                        CadenceDestination.Apps -> {
                            item {
                                SectionHeader(
                                    title = "Apps",
                                    subtitle = "All apps used today, ordered by time spent.",
                                    todayLabel = state.todayLabel,
                                )
                            }
                            item {
                                AppsOverviewCard(
                                    apps = state.appUsage,
                                    totalFocusedTime = state.totalFocusedTime,
                                )
                            }
                        }

                        CadenceDestination.Activity -> {
                            item {
                                SectionHeader(
                                    title = "Activity",
                                    subtitle = "A quick look at your latest tracked activity.",
                                    todayLabel = state.todayLabel,
                                )
                            }
                            item {
                                ActivityOverviewContent(state = state)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardHeader(
    greetingName: String,
    todayLabel: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column {
            Text(
                text = "Good morning, $greetingName \uD83D\uDC4B",
                style = MaterialTheme.typography.headlineLarge,
                color = CadenceTextPrimary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Stay consistent. Small steps, big progress.",
                style = MaterialTheme.typography.bodyLarge,
                color = CadenceTextSecondary,
            )
        }
        HeaderActions(todayLabel = todayLabel)
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    todayLabel: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = CadenceTextPrimary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = CadenceTextSecondary,
            )
        }
        HeaderActions(todayLabel = todayLabel)
    }
}

@Composable
private fun HeaderActions(todayLabel: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        HeaderPill(
            text = todayLabel,
            leadingIcon = Icons.Outlined.CalendarMonth,
            width = 118.dp,
        )
        HeaderSquareIcon(icon = Icons.Outlined.DarkMode)
        HeaderSquareIcon(icon = Icons.Outlined.Settings)
    }
}

@Composable
private fun HeaderPill(
    text: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    width: Dp,
) {
    Row(
        modifier = Modifier
            .width(width)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = CadenceTextPrimary,
        )
        Icon(imageVector = leadingIcon, contentDescription = null, tint = CadenceTextSecondary)
    }
}

@Composable
private fun HeaderSquareIcon(icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(11.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = CadenceTextPrimary)
    }
}

@Composable
private fun MetricSection(
    metrics: List<MetricCardData>,
    layoutMode: DashboardLayoutMode,
) {
    when (layoutMode) {
        DashboardLayoutMode.Expanded -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                metrics.forEach { metric ->
                    MetricCard(
                        data = metric,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        DashboardLayoutMode.Medium -> {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                metrics.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        rowItems.forEach { metric ->
                            MetricCard(
                                data = metric,
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        DashboardLayoutMode.Compact -> {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                metrics.forEach { metric ->
                    MetricCard(
                        data = metric,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpandedContent(state: DashboardUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            modifier = Modifier.weight(1.45f),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            ActivityChartCard(series = state.activitySeries, modifier = Modifier.fillMaxWidth())
            TopAppsCard(
                apps = state.appUsage.take(6),
                totalFocusedTime = state.totalFocusedTime,
                modifier = Modifier.fillMaxWidth(),
            )
            ActivityTimeline(segments = state.timeline, modifier = Modifier.fillMaxWidth())
        }
        Column(
            modifier = Modifier.width(320.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            CurrentFocusCard(
                appName = state.activeAppName,
                windowTitle = state.activeWindowTitle,
                activeSessionDuration = state.activeSessionDuration,
                details = state.currentFocusDetails,
                modifier = Modifier.fillMaxWidth(),
            )
            BreakCard(data = state.breakInfo, modifier = Modifier.fillMaxWidth())
            TrackingCard(data = state.trackingStatus, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun MediumContent(state: DashboardUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        ActivityChartCard(series = state.activitySeries, modifier = Modifier.fillMaxWidth())
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier.weight(1.2f),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                TopAppsCard(
                    apps = state.appUsage.take(6),
                    totalFocusedTime = state.totalFocusedTime,
                    modifier = Modifier.fillMaxWidth(),
                )
                TrackingCard(
                    data = state.trackingStatus,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Column(
                modifier = Modifier.weight(0.9f),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                CurrentFocusCard(
                    appName = state.activeAppName,
                    windowTitle = state.activeWindowTitle,
                    activeSessionDuration = state.activeSessionDuration,
                    details = state.currentFocusDetails,
                    modifier = Modifier.fillMaxWidth(),
                )
                BreakCard(data = state.breakInfo, modifier = Modifier.fillMaxWidth())
            }
        }
        ActivityTimeline(
            segments = state.timeline,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun CompactContent(state: DashboardUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        CurrentFocusCard(
            appName = state.activeAppName,
            windowTitle = state.activeWindowTitle,
            activeSessionDuration = state.activeSessionDuration,
            details = state.currentFocusDetails,
            modifier = Modifier.fillMaxWidth(),
        )
        ActivityChartCard(series = state.activitySeries, modifier = Modifier.fillMaxWidth())
        TopAppsCard(
            apps = state.appUsage.take(6),
            totalFocusedTime = state.totalFocusedTime,
            modifier = Modifier.fillMaxWidth(),
        )
        BreakCard(data = state.breakInfo, modifier = Modifier.fillMaxWidth())
        TrackingCard(data = state.trackingStatus, modifier = Modifier.fillMaxWidth())
        ActivityTimeline(segments = state.timeline, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun ActivityOverviewContent(state: DashboardUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        ActivityChartCard(series = state.activitySeries, modifier = Modifier.fillMaxWidth())
        ActivityTimeline(segments = state.timeline, modifier = Modifier.fillMaxWidth())
        TrackingCard(data = state.trackingStatus, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun AppsOverviewCard(
    apps: List<AppUsageData>,
    totalFocusedTime: String,
) {
    DashboardCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "All Apps Used",
                        style = MaterialTheme.typography.titleLarge,
                        color = CadenceTextPrimary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$totalFocusedTime tracked across ${apps.size} apps",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CadenceTextSecondary,
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8F9FD))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = "High to low",
                        style = MaterialTheme.typography.bodySmall,
                        color = CadenceTextPrimary,
                    )
                }
            }

            if (apps.isEmpty()) {
                EmptyAppsState()
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    apps.forEach { app ->
                        FullAppUsageRow(app = app)
                    }
                }
            }
        }
    }
}

@Composable
private fun FullAppUsageRow(app: AppUsageData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(26.dp)
                .height(26.dp)
                .clip(CircleShape)
                .background(app.color.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .height(10.dp)
                    .clip(CircleShape)
                    .background(app.color),
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = app.name,
            style = MaterialTheme.typography.bodyMedium,
            color = CadenceTextPrimary,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = app.duration,
            style = MaterialTheme.typography.bodyMedium,
            color = CadenceTextPrimary,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.width(14.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(CircleShape)
                .background(CadenceGraySoft),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(app.share.coerceIn(0f, 1f))
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(app.color),
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = app.shareLabel,
            style = MaterialTheme.typography.bodySmall,
            color = CadenceTextSecondary,
        )
    }
}

@Composable
private fun EmptyAppsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(CadenceGraySoft.copy(alpha = 0.6f))
                .padding(14.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Apps,
                contentDescription = null,
                tint = CadencePurple,
            )
        }
        Text(
            text = "No app activity yet",
            style = MaterialTheme.typography.titleMedium,
            color = CadenceTextPrimary,
        )
        Text(
            text = "App usage will appear here as soon as tracking captures it.",
            style = MaterialTheme.typography.bodyMedium,
            color = CadenceTextSecondary,
            textAlign = TextAlign.Center,
        )
    }
}
