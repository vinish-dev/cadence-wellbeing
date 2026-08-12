package com.vinish.cadence.ui.apps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vinish.cadence.ui.apps.components.AppUsageCard
import com.vinish.cadence.ui.dashboard.AppUsageData

@Composable
fun AppsScreen(
    apps: List<AppUsageData>,
    totalFocusedTime: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        AppUsageCard(
            apps = apps,
            totalFocusedTime = totalFocusedTime,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
