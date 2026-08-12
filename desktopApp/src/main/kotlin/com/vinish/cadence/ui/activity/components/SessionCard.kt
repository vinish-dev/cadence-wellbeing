package com.vinish.cadence.ui.activity.components
import com.vinish.cadence.ui.dashboard.AppUsageData
import com.vinish.cadence.ui.components.common.DashboardCard
import com.vinish.cadence.ui.activity.SessionSummaryData
import com.vinish.cadence.ui.activity.TimelineSegmentData

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


import com.vinish.cadence.ui.theme.CadenceGraySoft
import com.vinish.cadence.ui.theme.CadenceTextPrimary
import com.vinish.cadence.ui.theme.CadenceTextSecondary

@Composable
fun SessionCard(
    session: SessionSummaryData,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    DashboardCard(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = session.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = CadenceTextPrimary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = session.timeRange,
                        style = MaterialTheme.typography.bodyMedium,
                        color = CadenceTextSecondary,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = session.duration,
                        style = MaterialTheme.typography.titleMedium,
                        color = CadenceTextPrimary,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    val rotation by animateFloatAsState(if (expanded) 180f else 0f)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(CadenceGraySoft)
                            .padding(8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowDown,
                            contentDescription = null,
                            tint = CadenceTextPrimary,
                            modifier = Modifier.rotate(rotation),
                        )
                    }
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(18.dp))
                    ActivityTimeline(
                        segments = session.timeline,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Apps Used",
                        style = MaterialTheme.typography.titleMedium,
                        color = CadenceTextPrimary,
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        session.apps.forEach { app ->
                            SessionAppUsageRow(app = app)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SessionAppUsageRow(app: AppUsageData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(app.color.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
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
