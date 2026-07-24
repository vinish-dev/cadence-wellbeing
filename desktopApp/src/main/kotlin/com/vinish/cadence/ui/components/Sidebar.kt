package com.vinish.cadence.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vinish.cadence.ui.navigation.CadenceDestination
import com.vinish.cadence.ui.theme.CadenceBackground
import com.vinish.cadence.ui.theme.CadenceBorder
import com.vinish.cadence.ui.theme.CadencePurple
import com.vinish.cadence.ui.theme.CadencePurpleSoft
import com.vinish.cadence.ui.theme.CadenceTextPrimary
import com.vinish.cadence.ui.theme.CadenceTextSecondary

private data class SidebarItemUi(
    val destination: CadenceDestination,
    val icon: ImageVector,
)

@Composable
fun Sidebar(
    selectedDestination: CadenceDestination,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        SidebarItemUi(CadenceDestination.Dashboard, Icons.Outlined.Dashboard),
        SidebarItemUi(CadenceDestination.Activity, Icons.Outlined.AutoGraph),
        SidebarItemUi(CadenceDestination.Apps, Icons.Outlined.Apps),
    )

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(if (compact) 100.dp else 180.dp)
            .background(Color.White)
            .padding(horizontal = 14.dp, vertical = 20.dp),
    ) {
        CadenceWordmark(compact = compact)
        Spacer(modifier = Modifier.height(28.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items.forEach { item ->
                SidebarItem(
                    label = item.destination.label,
                    icon = item.icon,
                    selected = item.destination == selectedDestination,
                    compact = compact,
                )
            }
        }
    }
}

@Composable
private fun CadenceWordmark(compact: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (compact) Arrangement.Center else Arrangement.Start,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF4E7BFF), Color(0xFF7C52FF), Color(0xFF58D8C6)),
                    ),
                ),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f)),
            )
        }
        if (!compact) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Cadence",
                style = MaterialTheme.typography.titleLarge,
                color = CadenceTextPrimary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun SidebarItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    compact: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) CadencePurpleSoft else CadenceBackground)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (compact) Arrangement.Center else Arrangement.Start,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) CadencePurple else CadenceTextSecondary,
            modifier = Modifier.size(18.dp),
        )
        if (!compact) {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) CadencePurple else CadenceTextPrimary,
            )
        }
    }
}
