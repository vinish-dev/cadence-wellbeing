package com.vinish.cadence.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vinish.cadence.ui.dashboard.components.ActivityChartCard
import com.vinish.cadence.ui.dashboard.components.BreakCard
import com.vinish.cadence.ui.dashboard.components.CurrentFocusCard
import com.vinish.cadence.ui.dashboard.components.MetricCard
import com.vinish.cadence.ui.dashboard.components.TopAppsCard
import com.vinish.cadence.ui.dashboard.components.TrackingCard
import com.vinish.cadence.ui.activity.components.ActivityTimeline

enum class DashboardLayoutMode {
    Compact,
    Medium,
    Expanded,
}

@Composable
fun DashboardScreenContent(
    state: DashboardUiState,
    layoutMode: DashboardLayoutMode,
    showActivityChart: Boolean,
    showSessionActiveCard: Boolean,
    onSnoozeClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        MetricSection(
            metrics = state.metricCards,
            layoutMode = layoutMode,
        )
        when (layoutMode) {
            DashboardLayoutMode.Expanded -> ExpandedContent(state, showActivityChart, showSessionActiveCard, onSnoozeClick)
            DashboardLayoutMode.Medium -> MediumContent(state, showActivityChart, showSessionActiveCard, onSnoozeClick)
            DashboardLayoutMode.Compact -> CompactContent(state, showActivityChart, showSessionActiveCard, onSnoozeClick)
        }
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
private fun ExpandedContent(state: DashboardUiState, showActivityChart: Boolean, showSessionActiveCard: Boolean, onSnoozeClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            modifier = Modifier.weight(1.45f),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            if (showActivityChart) {
                ActivityChartCard(series = state.activitySeries, modifier = Modifier.fillMaxWidth())
            }
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
            BreakCard(data = state.breakInfo, onSnoozeClick = onSnoozeClick, modifier = Modifier.fillMaxWidth())
            if (showSessionActiveCard) {
                TrackingCard(data = state.trackingStatus, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun MediumContent(state: DashboardUiState, showActivityChart: Boolean, showSessionActiveCard: Boolean, onSnoozeClick: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        if (showActivityChart) {
            ActivityChartCard(series = state.activitySeries, modifier = Modifier.fillMaxWidth())
        }
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
                if (showSessionActiveCard) {
                    TrackingCard(
                        data = state.trackingStatus,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
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
                BreakCard(data = state.breakInfo, onSnoozeClick = onSnoozeClick, modifier = Modifier.fillMaxWidth())
            }
        }
        ActivityTimeline(
            segments = state.timeline,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun CompactContent(state: DashboardUiState, showActivityChart: Boolean, showSessionActiveCard: Boolean, onSnoozeClick: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        CurrentFocusCard(
            appName = state.activeAppName,
            windowTitle = state.activeWindowTitle,
            activeSessionDuration = state.activeSessionDuration,
            details = state.currentFocusDetails,
            modifier = Modifier.fillMaxWidth(),
        )
        if (showActivityChart) {
            ActivityChartCard(series = state.activitySeries, modifier = Modifier.fillMaxWidth())
        }
        TopAppsCard(
            apps = state.appUsage.take(6),
            totalFocusedTime = state.totalFocusedTime,
            modifier = Modifier.fillMaxWidth(),
        )
        BreakCard(data = state.breakInfo, onSnoozeClick = onSnoozeClick, modifier = Modifier.fillMaxWidth())
        if (showSessionActiveCard) {
            TrackingCard(data = state.trackingStatus, modifier = Modifier.fillMaxWidth())
        }
        ActivityTimeline(segments = state.timeline, modifier = Modifier.fillMaxWidth())
    }
}
