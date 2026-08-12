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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.vinish.cadence.ui.theme.CadenceTextPrimary
import com.vinish.cadence.ui.theme.CadenceTextSecondary

@Composable
fun MetricCard(
    data: MetricCardData,
    modifier: Modifier = Modifier,
) {
    DashboardCard(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = data.value,
                    style = MaterialTheme.typography.headlineMedium,
                    color = CadenceTextPrimary,
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(data.iconBackground),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = data.icon,
                        contentDescription = data.title,
                        tint = data.iconTint,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = data.title,
                style = MaterialTheme.typography.bodyMedium,
                color = CadenceTextSecondary,
            )
            if (data.trend.isNotBlank() || data.caption.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (data.trend.isNotBlank()) {
                        val trendColor = if (data.trendPositive) Color(0xFF1DBA68) else Color(0xFFE04F5F)
                        Icon(
                            imageVector = if (data.trendPositive) Icons.Rounded.ArrowUpward else Icons.Rounded.ArrowDownward,
                            contentDescription = null,
                            tint = trendColor,
                            modifier = Modifier.size(14.dp),
                        )
                        Text(
                            text = data.trend,
                            style = MaterialTheme.typography.labelLarge,
                            color = trendColor,
                        )
                        Text(
                            text = "  ${data.caption}",
                            style = MaterialTheme.typography.bodySmall,
                            color = CadenceTextSecondary,
                        )
                    } else {
                        Text(
                            text = data.caption,
                            style = MaterialTheme.typography.bodySmall,
                            color = CadenceTextSecondary,
                        )
                    }
                }
            }
        }
    }
}
