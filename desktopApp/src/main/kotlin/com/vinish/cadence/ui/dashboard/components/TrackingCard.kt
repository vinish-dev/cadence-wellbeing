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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PauseCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.vinish.cadence.ui.theme.CadenceGreen
import com.vinish.cadence.ui.theme.CadenceTextPrimary
import com.vinish.cadence.ui.theme.CadenceTextSecondary

@Composable
fun TrackingCard(
    data: TrackingStatusData,
    modifier: Modifier = Modifier,
) {
    DashboardCard(modifier = modifier) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CadenceGreen)
                        .padding(5.dp),
                )
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = if (data.isActive) "Session active" else "Session paused",
                    style = MaterialTheme.typography.labelLarge,
                    color = CadenceTextPrimary,
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Since ${data.startedAt}",
                style = MaterialTheme.typography.bodyMedium,
                color = CadenceTextSecondary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = data.trackedToday,
                style = MaterialTheme.typography.headlineLarge,
                color = CadenceTextPrimary,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tracked time today",
                style = MaterialTheme.typography.bodySmall,
                color = CadenceTextSecondary,
            )
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFFF4F1FF))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.PauseCircle,
                    contentDescription = null,
                    tint = Color(0xFF6C63FF),
                )
                Text(
                    text = data.actionLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF6C63FF),
                )
            }
        }
    }
}
