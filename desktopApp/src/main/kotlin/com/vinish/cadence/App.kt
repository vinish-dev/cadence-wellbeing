package com.vinish.cadence

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.vinish.cadence.tracking.AppTracker
import com.vinish.cadence.tracking.KeyboardTracker
import com.vinish.cadence.ui.navigation.CadenceDestination
import com.vinish.cadence.ui.screens.DashboardScreen
import com.vinish.cadence.ui.screens.dashboardStateFromTracking
import com.vinish.cadence.ui.theme.CadenceTheme

@Composable
fun App() {
    LaunchedEffect(Unit) {
        AppTracker.start()
        KeyboardTracker.start()
    }

    val trackerState by AppTracker.state.collectAsState()
    val typingCount by KeyboardTracker.typingCount.collectAsState()
    val dashboardState = remember(trackerState, typingCount) {
        dashboardStateFromTracking(
            typingCount = typingCount,
            trackerState = trackerState,
        )
    }
    var selectedDestination by remember { mutableStateOf(CadenceDestination.Dashboard) }

    CadenceTheme {
        DashboardScreen(
            state = dashboardState,
            selectedDestination = selectedDestination,
            onDestinationSelected = { selectedDestination = it },
        )
    }
}
