package com.vinish.cadence.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vinish.cadence.ui.screens.TimelineSegmentData
import com.vinish.cadence.ui.theme.CadenceGraySoft
import com.vinish.cadence.ui.theme.CadenceTextPrimary
import com.vinish.cadence.ui.theme.CadenceTextSecondary

@Composable
fun ActivityTimeline(
    segments: List<TimelineSegmentData>,
    modifier: Modifier = Modifier,
) {
    DashboardCard(modifier = modifier) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Today's Activity Timeline",
                    style = MaterialTheme.typography.titleMedium,
                    color = CadenceTextPrimary,
                )
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = CadenceTextSecondary,
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                segments.forEach { segment ->
                    Box(
                        modifier = Modifier
                            .weight(segment.weight)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (segment.label.isBlank()) CadenceGraySoft else segment.color),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (segment.label.isNotBlank()) {
                            Text(
                                text = segment.label,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                segments.forEach { segment ->
                    Text(
                        text = segment.startTime,
                        modifier = Modifier.weight(segment.weight),
                        style = MaterialTheme.typography.bodySmall,
                        color = CadenceTextSecondary,
                        textAlign = TextAlign.Start,
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Click on any block to see session details",
                style = MaterialTheme.typography.bodySmall,
                color = CadenceTextSecondary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
    }
}
