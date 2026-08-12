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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

import com.vinish.cadence.ui.theme.CadenceBorder
import com.vinish.cadence.ui.theme.CadenceTextPrimary
import com.vinish.cadence.ui.theme.CadenceTextSecondary

@Composable
fun ActivityChartCard(
    series: List<ChartPointSet>,
    modifier: Modifier = Modifier,
) {
    DashboardCard(modifier = modifier) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Activity Overview",
                        style = MaterialTheme.typography.titleMedium,
                        color = CadenceTextPrimary,
                    )
                    Spacer(modifier = Modifier.height(0.dp))
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = CadenceTextSecondary,
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }
                FilterChipLabel("Today")
            }
            Spacer(modifier = Modifier.height(18.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                series.forEach { set ->
                    LegendItem(label = set.label, color = set.color)
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            ChartYAxisLabels()
            Spacer(modifier = Modifier.height(6.dp))
            ActivityLineChart(series = series)
            Spacer(modifier = Modifier.height(14.dp))
            ChartXAxisLabels()
        }
    }
}

@Composable
private fun FilterChipLabel(label: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8F9FD))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = CadenceTextPrimary,
        )
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = null,
            tint = CadenceTextSecondary,
            modifier = Modifier.padding(start = 6.dp),
        )
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(color)
                .padding(4.dp),
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = CadenceTextSecondary,
        )
    }
}

@Composable
private fun ChartYAxisLabels() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text("0%", style = MaterialTheme.typography.bodySmall, color = CadenceTextSecondary)
        Text("25%", style = MaterialTheme.typography.bodySmall, color = CadenceTextSecondary)
        Text("50%", style = MaterialTheme.typography.bodySmall, color = CadenceTextSecondary)
        Text("75%", style = MaterialTheme.typography.bodySmall, color = CadenceTextSecondary)
        Text("100%", style = MaterialTheme.typography.bodySmall, color = CadenceTextSecondary)
    }
}

@Composable
private fun ActivityLineChart(series: List<ChartPointSet>) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
    ) {
        val gridLines = 4
        repeat(gridLines + 1) { index ->
            val y = size.height * index / gridLines
            drawLine(
                color = CadenceBorder,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx(),
            )
        }

        series.forEachIndexed { seriesIndex, set ->
            val fillPath = Path()
            val linePath = Path()
            set.values.forEachIndexed { index, value ->
                val x = if (set.values.size == 1) 0f else size.width * index / (set.values.lastIndex)
                val y = size.height - (value * size.height)
                if (index == 0) {
                    linePath.moveTo(x, y)
                    fillPath.moveTo(x, size.height)
                    fillPath.lineTo(x, y)
                } else {
                    linePath.lineTo(x, y)
                    fillPath.lineTo(x, y)
                }
                if (index == set.values.lastIndex) {
                    fillPath.lineTo(x, size.height)
                    fillPath.close()
                }
            }
            if (seriesIndex == 0) {
                drawPath(path = fillPath, color = set.color.copy(alpha = 0.12f))
            }
            drawPath(
                path = linePath,
                color = set.color,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
            )
        }
    }
}

@Composable
private fun ChartXAxisLabels() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        listOf("12 AM", "4 AM", "8 AM", "12 PM", "4 PM", "8 PM", "12 AM").forEach { label ->
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = CadenceTextSecondary,
            )
        }
    }
}
