package com.vinish.cadence.ui.dashboard.components
import com.vinish.cadence.ui.components.common.DashboardCard
import com.vinish.cadence.ui.dashboard.MetricCardData
import com.vinish.cadence.ui.dashboard.BreakInfoData
import com.vinish.cadence.ui.dashboard.TrackingStatusData
import com.vinish.cadence.ui.dashboard.ChartPointSet
import com.vinish.cadence.ui.dashboard.FocusDetail
import com.vinish.cadence.ui.dashboard.AppUsageData

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.vinish.cadence.ui.theme.CadenceGreen
import com.vinish.cadence.ui.theme.CadenceGreenSoft
import com.vinish.cadence.ui.theme.CadenceTextPrimary
import com.vinish.cadence.ui.theme.CadenceTextSecondary

@Composable
fun CurrentFocusCard(
    appName: String,
    windowTitle: String,
    activeSessionDuration: String,
    modifier: Modifier = Modifier,
) {
    DashboardCard(modifier = modifier) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Current Focus",
                    style = MaterialTheme.typography.titleMedium,
                    color = CadenceTextPrimary,
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(CadenceGreenSoft)
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                ) {
                    Text(
                        text = "Active",
                        style = MaterialTheme.typography.labelMedium,
                        color = CadenceGreen,
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            //icon, app name title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFF0E7)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Public,
                        contentDescription = null,
                        tint = Color(0xFFFF6A00),
                        modifier = Modifier.size(28.dp),
                    )
                }
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(14.dp))
                Column {
                    Text(
                        text = appName,
                        style = MaterialTheme.typography.titleMedium,
                        color = CadenceTextPrimary,
                    )
                    Text(
                        text = windowTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = CadenceTextSecondary,
                    )
                }
            }
            //text
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = activeSessionDuration,
                style = MaterialTheme.typography.headlineMedium,
                color = CadenceTextSecondary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))

        }
    }
}
