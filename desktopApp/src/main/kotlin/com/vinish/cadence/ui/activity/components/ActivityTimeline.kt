package com.vinish.cadence.ui.activity.components
import com.vinish.cadence.ui.components.common.DashboardCard
import com.vinish.cadence.ui.activity.SessionSummaryData
import com.vinish.cadence.ui.activity.TimelineSegmentData

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import com.vinish.cadence.ui.theme.CadenceGraySoft
import com.vinish.cadence.ui.theme.CadenceTextPrimary
import com.vinish.cadence.ui.theme.CadenceTextSecondary

private val TimelineBlockHeight = 28.dp
private val TimelineBlockMinWidth = 48.dp
private val TimelineLabelMinWidth = 48.dp

@Composable
fun ActivityTimeline(
    segments: List<TimelineSegmentData>,
    modifier: Modifier = Modifier,
) {
    DashboardCard(modifier = modifier) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Session Timeline",
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
            
            val scrollState = rememberScrollState()
            LaunchedEffect(segments.size) {
                if (segments.isNotEmpty()) {
                    scrollState.scrollTo(scrollState.maxValue)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier.height(TimelineBlockHeight),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    segments.forEach { segment ->
                        // Calculate width based on duration: 0.15 dp per second, min 48dp
                        val calculatedWidth = (segment.weight * 0.15f).dp.coerceAtLeast(TimelineBlockMinWidth)
                        BoxWithConstraints(
                            modifier = Modifier
                                .width(calculatedWidth)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (segment.label.isBlank()) CadenceGraySoft else segment.color),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (segment.label.isNotBlank() && maxWidth >= TimelineLabelMinWidth) {
                                Text(
                                    text = segment.label,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    segments.forEach { segment ->
                        val calculatedWidth = (segment.weight * 0.15f).dp.coerceAtLeast(TimelineBlockMinWidth)
                        Text(
                            text = segment.startTime,
                            modifier = Modifier.width(calculatedWidth),
                            style = MaterialTheme.typography.bodySmall,
                            color = CadenceTextSecondary,
                            textAlign = TextAlign.Start,
                        )
                    }
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
