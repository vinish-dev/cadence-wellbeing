package com.vinish.cadence.ui.dashboard.components
import com.vinish.cadence.ui.components.common.DashboardCard
import com.vinish.cadence.ui.dashboard.MetricCardData
import com.vinish.cadence.ui.dashboard.BreakInfoData
import com.vinish.cadence.ui.dashboard.TrackingStatusData
import com.vinish.cadence.ui.dashboard.ChartPointSet
import com.vinish.cadence.ui.dashboard.FocusDetail
import com.vinish.cadence.ui.dashboard.AppUsageData

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.vinish.cadence.ui.theme.CadenceGraySoft
import com.vinish.cadence.ui.theme.CadencePurple
import com.vinish.cadence.ui.theme.CadenceTextPrimary
import com.vinish.cadence.ui.theme.CadenceTextSecondary

@Composable
fun BreakCard(
    data: BreakInfoData,
    onSnoozeClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    DashboardCard(modifier = modifier) {
        Column {
            Text(
                text = "Next Break",
                style = MaterialTheme.typography.titleMedium,
                color = CadenceTextPrimary,
            )
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.size(118.dp)) {
                        val stroke = 10.dp.toPx()
                        drawArc(
                            color = CadenceGraySoft,
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = stroke),
                            size = Size(size.width, size.height),
                        )
                        
                        val sweep = if (data.isRecommendationActive) 360f else {
                            val progress = (data.currentFocusMinutes.toFloat() / 45f).coerceIn(0f, 1f)
                            progress * 360f
                        }
                        val strokeColor = if (data.isRecommendationActive) com.vinish.cadence.ui.theme.CadenceOrange else CadencePurple

                        drawArc(
                            color = strokeColor,
                            startAngle = -90f,
                            sweepAngle = sweep,
                            useCenter = false,
                            style = Stroke(width = stroke, cap = StrokeCap.Round),
                            size = Size(size.width, size.height),
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (data.isRecommendationActive) {
                            androidx.compose.material3.Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Warning,
                                contentDescription = "Break Recommended",
                                tint = com.vinish.cadence.ui.theme.CadenceOrange,
                                modifier = Modifier.size(32.dp)
                            )
                        } else {
                            Text(
                                text = "${data.nextBreakMinutes}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = CadenceTextPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "min",
                                style = MaterialTheme.typography.bodySmall,
                                color = CadenceTextSecondary,
                            )
                        }
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    val statusText = when {
                        data.isRecommendationActive -> "Time for a break! Step away to recharge."
                        data.isSnoozed -> "Break snoozed. Next break in ${data.nextBreakMinutes} minutes"
                        else -> "Next break in ${data.nextBreakMinutes} minutes"
                    }
                    
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = CadenceTextPrimary,
                    )
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Text(
                        text = "You've been focused for ${data.currentFocusMinutes} minutes.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CadenceTextSecondary,
                    )
                    
                    if (data.isRecommendationActive) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)){
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFE9EBFF))
                                    .clickable(onClick = onSnoozeClick)
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                            ) {
                                Text(
                                    text = "Later",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = CadenceTextSecondary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
