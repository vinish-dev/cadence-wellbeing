package com.vinish.cadence.ui.components.common

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
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vinish.cadence.ui.theme.CadenceTextPrimary
import com.vinish.cadence.ui.theme.CadenceTextSecondary

@Composable
fun DashboardHeader(
    greetingName: String,
    todayLabel: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column {
            Text(
                text = "Good Morning, $greetingName",
                style = MaterialTheme.typography.headlineLarge,
                color = CadenceTextPrimary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You're off to a productive start today.",
                style = MaterialTheme.typography.bodyLarge,
                color = CadenceTextSecondary,
            )
        }
        HeaderActions(todayLabel = todayLabel)
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    todayLabel: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = CadenceTextPrimary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = CadenceTextSecondary,
            )
        }
        HeaderActions(todayLabel = todayLabel)
    }
}

@Composable
private fun HeaderActions(todayLabel: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        HeaderPill(
            text = todayLabel,
            leadingIcon = Icons.Outlined.CalendarMonth,
            width = 118.dp,
        )
        HeaderSquareIcon(icon = Icons.Outlined.DarkMode)
        HeaderSquareIcon(icon = Icons.Outlined.Settings)
    }
}

@Composable
private fun HeaderPill(
    text: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    width: Dp,
) {
    Row(
        modifier = Modifier
            .width(width)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = CadenceTextPrimary,
        )
        Icon(imageVector = leadingIcon, contentDescription = null, tint = CadenceTextSecondary)
    }
}

@Composable
private fun HeaderSquareIcon(icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(11.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = CadenceTextPrimary)
    }
}
