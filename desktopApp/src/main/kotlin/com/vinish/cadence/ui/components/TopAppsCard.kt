package com.vinish.cadence.ui.components

import androidx.compose.foundation.Canvas
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
import com.vinish.cadence.ui.screens.AppUsageData
import com.vinish.cadence.ui.theme.CadenceBorder
import com.vinish.cadence.ui.theme.CadenceGraySoft
import com.vinish.cadence.ui.theme.CadenceTextPrimary
import com.vinish.cadence.ui.theme.CadenceTextSecondary

@Composable
fun TopAppsCard(
    apps: List<AppUsageData>,
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
                    text = "Top Apps Today",
                    style = MaterialTheme.typography.titleMedium,
                    color = CadenceTextPrimary,
                )
                Box(
                    modifier = Modifier
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8F9FD))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = "By time spent",
                        style = MaterialTheme.typography.bodySmall,
                        color = CadenceTextPrimary,
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(22.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1.2f),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    apps.forEach { app ->
                        AppUsageRow(app)
                    }
                }
                FocusDonut(
                    apps = apps,
                    modifier = Modifier.weight(0.8f),
                )
            }
        }
    }
}

@Composable
private fun AppUsageRow(app: AppUsageData) {
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
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = app.name,
            style = MaterialTheme.typography.bodyMedium,
            color = CadenceTextPrimary,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = app.duration,
            style = MaterialTheme.typography.bodySmall,
            color = CadenceTextSecondary,
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(12.dp))
        Box(
            modifier = Modifier
                .weight(1.2f)
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
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = app.shareLabel,
            style = MaterialTheme.typography.bodySmall,
            color = CadenceTextSecondary,
        )
    }
}

@Composable
private fun FocusDonut(
    apps: List<AppUsageData>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(184.dp)) {
            val stroke = 18.dp.toPx()
            var start = -90f
            apps.forEach { app ->
                val sweep = 360f * app.share
                drawArc(
                    color = app.color,
                    startAngle = start,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Butt),
                    size = Size(size.width, size.height),
                )
                start += sweep
            }
            if (start < 270f) {
                drawArc(
                    color = CadenceBorder,
                    startAngle = start,
                    sweepAngle = 270f - start,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Butt),
                    size = Size(size.width, size.height),
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "4h 22m",
                style = MaterialTheme.typography.headlineMedium,
                color = CadenceTextPrimary,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Total focused\ntime",
                style = MaterialTheme.typography.bodySmall,
                color = CadenceTextSecondary,
            )
        }
    }
}
