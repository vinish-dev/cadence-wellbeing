package com.vinish.cadence

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vinish.cadence.tracking.AppTracker
import com.vinish.cadence.tracking.KeyboardTracker
import com.vinish.cadence.tracking.SystemTracker
import com.vinish.cadence.tracking.SessionManager
import com.vinish.cadence.ui.activity.ActivityScreen
import com.vinish.cadence.ui.apps.AppsScreen
import com.vinish.cadence.ui.components.common.DashboardHeader
import com.vinish.cadence.ui.components.common.SectionHeader
import com.vinish.cadence.ui.dashboard.DashboardLayoutMode
import com.vinish.cadence.ui.dashboard.DashboardScreenContent
import com.vinish.cadence.ui.dashboard.dashboardStateFromTracking
import com.vinish.cadence.ui.navigation.CadenceDestination
import com.vinish.cadence.ui.navigation.Sidebar
import com.vinish.cadence.ui.theme.CadenceBackground
import com.vinish.cadence.ui.theme.CadenceTheme

@Composable
fun App() {
    LaunchedEffect(Unit) {
        SystemTracker.start()
        SessionManager.start()
        AppTracker.start()
        KeyboardTracker.start()
    }

    val trackerState by AppTracker.state.collectAsState()
    val typingCount by KeyboardTracker.typingCount.collectAsState()
    val currentSession by SessionManager.currentSession.collectAsState()
    val pastSessions by SessionManager.sessions.collectAsState()
    val settingsState by com.vinish.cadence.tracking.SettingsManager.settings.collectAsState()
    val dashboardState = remember(trackerState, typingCount, currentSession, pastSessions) {
        dashboardStateFromTracking(
            typingCount = typingCount,
            trackerState = trackerState,
            currentSession = currentSession,
            pastSessions = pastSessions,
        )
    }
    var selectedDestination by remember { mutableStateOf(CadenceDestination.Dashboard) }

    CadenceTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
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
                        onDestinationSelected = { selectedDestination = it },
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
                                        greetingName = dashboardState.greetingName,
                                        todayLabel = dashboardState.todayLabel,
                                        onSettingsClick = { selectedDestination = CadenceDestination.Settings },
                                    )
                                }
                                item {
                                    DashboardScreenContent(
                                        state = dashboardState,
                                        layoutMode = layoutMode,
                                        showActivityChart = settingsState.showActivityOverviewChart
                                    )
                                }
                            }
                            CadenceDestination.Settings -> {
                                item {
                                    SectionHeader(
                                        title = "Settings",
                                        subtitle = "Manage your dashboard preferences.",
                                        todayLabel = dashboardState.todayLabel,
                                        onSettingsClick = { selectedDestination = CadenceDestination.Settings }
                                    )
                                }
                                item {
                                    com.vinish.cadence.ui.settings.SettingsScreen(
                                        state = settingsState,
                                        onToggleActivityChart = { com.vinish.cadence.tracking.SettingsManager.toggleActivityOverviewChart(it) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            CadenceDestination.Activity -> {
                                item {
                                    SectionHeader(
                                        title = "Activity",
                                        subtitle = "A quick look at your latest tracked activity.",
                                        todayLabel = dashboardState.todayLabel,
                                        onSettingsClick = { selectedDestination = CadenceDestination.Settings },
                                    )
                                }
                                item {
                                    ActivityScreen(
                                        sessions = dashboardState.sessions,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            CadenceDestination.Apps -> {
                                item {
                                    SectionHeader(
                                        title = "Apps Usage",
                                        subtitle = "Detailed breakdown of the applications you've focused on today.",
                                        todayLabel = dashboardState.todayLabel,
                                        onSettingsClick = { selectedDestination = CadenceDestination.Settings },
                                    )
                                }
                                item {
                                    AppsScreen(
                                        apps = dashboardState.appUsage,
                                        totalFocusedTime = dashboardState.totalFocusedTime,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
