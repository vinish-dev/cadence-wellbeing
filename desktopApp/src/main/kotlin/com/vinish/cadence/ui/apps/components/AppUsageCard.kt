package com.vinish.cadence.ui.apps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vinish.cadence.ui.components.common.DashboardCard
import com.vinish.cadence.ui.dashboard.AppUsageData
import com.vinish.cadence.ui.theme.CadenceGraySoft
import com.vinish.cadence.ui.theme.CadencePurple
import com.vinish.cadence.ui.theme.CadenceTextPrimary
import com.vinish.cadence.ui.theme.CadenceTextSecondary

@Composable
fun AppUsageCard(
    apps: List<AppUsageData>,
    totalFocusedTime: String,
    modifier: Modifier = Modifier,
) {
    DashboardCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "All Apps Used",
                        style = MaterialTheme.typography.titleLarge,
                        color = CadenceTextPrimary,
                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = "$totalFocusedTime tracked across ${apps.size} apps",
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = CadenceTextSecondary,
//                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(com.vinish.cadence.ui.theme.CadenceBackground)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = totalFocusedTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = CadenceTextPrimary,
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
                    .fillMaxWidth(app.share.coerceIn(0.01f, 1f))
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
            text = "Your application usage will appear here once you start tracking.",
            style = MaterialTheme.typography.bodyMedium,
            color = CadenceTextSecondary,
        )
    }
}
