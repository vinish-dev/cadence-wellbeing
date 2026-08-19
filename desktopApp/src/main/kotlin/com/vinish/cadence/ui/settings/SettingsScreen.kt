package com.vinish.cadence.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.vinish.cadence.tracking.SettingsState
import com.vinish.cadence.ui.theme.CadencePurple
import com.vinish.cadence.ui.theme.CadenceTextPrimary
import com.vinish.cadence.ui.theme.CadenceTextSecondary

@Composable
fun SettingsScreen(
    state: SettingsState,
    onToggleActivityChart: (Boolean) -> Unit,
    onToggleSessionActiveCard: (Boolean) -> Unit,
    onToggleDonutChart: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Dashboard Preferences
        SettingsCard(
            icon = Icons.Outlined.GridView,
            title = "Dashboard Preferences",
            subtitle = "Choose what you want to see on your dashboard."
        ) {
            SettingsToggleRow(
                title = "Show Activity Overview Line Chart",
                subtitle = "Displays a timeline chart of your focus sessions on the dashboard.",
                icon = Icons.Outlined.ShowChart,
                checked = state.showActivityOverviewChart,
                onCheckedChange = onToggleActivityChart
            )
            SettingsToggleRow(
                title = "Show Session Active Card",
                subtitle = "Displays the current session tracking status on the dashboard.",
                icon = Icons.Outlined.Timer,
                checked = state.showSessionActiveCard,
                onCheckedChange = onToggleSessionActiveCard
            )
            SettingsToggleRow(
                title = "Show Top Apps Donut Chart",
                subtitle = "Displays a visual breakdown of your top applications.",
                icon = Icons.Outlined.PieChart,
                checked = state.showDonutChart,
                onCheckedChange = onToggleDonutChart
            )
            SettingsInputRow(
                title = "Display Name",
                subtitle = "How you want to be greeted on the dashboard.",
                icon = Icons.Outlined.Person,
                value = state.userName,
                onValueChange = { com.vinish.cadence.tracking.SettingsManager.updateUserName(it) }
            )
        }

        // 2. Break Timer
        SettingsCard(
            icon = Icons.Outlined.Schedule,
            title = "Break Timer",
            subtitle = "Set how often you'd like to be reminded to take a break.",
            trailingContent = {
                Text(
                    text = "Current: ${state.breakTimerMinutes} minutes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CadenceTextSecondary
                )
            }
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(start = 56.dp)) {
                Slider(
                    value = state.breakTimerMinutes.toFloat(),
                    onValueChange = { com.vinish.cadence.tracking.SettingsManager.updateBreakTimer(it.toInt()) },
                    valueRange = 15f..120f,
                    steps = 20,
                    colors = SliderDefaults.colors(
                        thumbColor = CadencePurple,
                        activeTrackColor = CadencePurple,
                        inactiveTrackColor = Color(0xFFE5E7EB)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("15 min", style = MaterialTheme.typography.bodySmall, color = CadenceTextSecondary)
                    Text("120 min", style = MaterialTheme.typography.bodySmall, color = CadenceTextSecondary)
                }
            }
        }

        // 3. Notifications
        SettingsCard(
            icon = Icons.Outlined.Notifications,
            title = "Notifications",
            subtitle = "Control how and when Cadence alerts you."
        ) {
            SettingsToggleRow(
                title = "Break Reminders",
                subtitle = "Receive a notification when your break timer is up.",
                icon = Icons.Outlined.Alarm,
                iconTint = CadencePurple,
                checked = state.breakRemindersEnabled,
                onCheckedChange = { com.vinish.cadence.tracking.SettingsManager.toggleBreakReminders(it) }
            )
            SettingsToggleRow(
                title = "Notification Sound",
                subtitle = "Play a sound with notifications.",
                icon = Icons.Outlined.VolumeUp,
                iconTint = Color(0xFFF59E0B), // Orange/Gold
                checked = state.notificationSoundEnabled,
                onCheckedChange = { com.vinish.cadence.tracking.SettingsManager.toggleNotificationSound(it) }
            )
            SettingsToggleRow(
                title = "Smart Break Notifications",
                subtitle = "Get notified when Cadence automatically detects you took a break.",
                icon = Icons.Outlined.AutoAwesome,
                iconTint = Color(0xFF10B981), // Green
                checked = state.smartBreakNotificationsEnabled,
                onCheckedChange = { com.vinish.cadence.tracking.SettingsManager.toggleSmartBreakNotifications(it) }
            )
        }

        // 4. Tracking & Privacy
        SettingsCard(
            icon = Icons.Outlined.Shield,
            title = "Tracking & Privacy",
            subtitle = "Control what Cadence tracks to help improve your insights."
        ) {
            SettingsToggleRow(
                title = "Activity Detection",
                subtitle = "Used to determine whether you're actively using your computer for break detection.",
                icon = Icons.Outlined.MonitorHeart,
                iconTint = Color(0xFF10B981), // Green color for activity detection icon like screenshot
                checked = state.isActivityDetectionEnabled,
                onCheckedChange = { com.vinish.cadence.tracking.SettingsManager.toggleActivityDetection(it) }
            )
            SettingsToggleRow(
                title = "Track Keyboard Activity",
                subtitle = "Count keyboard presses for the Keys Typed metric.",
                icon = Icons.Outlined.Keyboard,
                iconTint = Color(0xFF3B82F6), // Blue
                checked = state.isKeyboardTrackingEnabled,
                onCheckedChange = { com.vinish.cadence.tracking.SettingsManager.toggleKeyboardTracking(it) }
            )
            SettingsToggleRow(
                title = "Track Mouse Activity",
                subtitle = "Count mouse clicks for the Mouse Clicks metric.",
                icon = Icons.Outlined.Mouse,
                iconTint = Color(0xFFF59E0B), // Orange
                checked = state.isMouseTrackingEnabled,
                onCheckedChange = { com.vinish.cadence.tracking.SettingsManager.toggleMouseTracking(it) }
            )
        }

        // 4. System & Data
        SettingsCard(
            icon = Icons.Outlined.Storage,
            title = "System & Data",
            subtitle = "Manage how Cadence runs and your local data."
        ) {
            SettingsToggleRow(
                title = "Run at Startup",
                subtitle = "Automatically start Cadence in the background when you sign in.",
                icon = Icons.Outlined.PowerSettingsNew,
                checked = state.runAtStartup,
                onCheckedChange = { com.vinish.cadence.tracking.SettingsManager.toggleRunAtStartup(it) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedButton(
                onClick = { com.vinish.cadence.tracking.SessionManager.clearTodayData() },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFDC2626) // Red color for destructive action
                ),
                border = BorderStroke(1.dp, Color(0xFFDC2626)),
                modifier = Modifier.fillMaxWidth().padding(start = 56.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Clear Data",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Clear Today's Data")
            }
            Text(
                text = "This will permanently delete all data collected today. This action cannot be undone.",
                style = MaterialTheme.typography.bodySmall,
                color = CadenceTextSecondary,
                modifier = Modifier.fillMaxWidth().padding(start = 56.dp, top = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun SettingsCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    trailingContent: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color(0xFFE5E7EB), shape = RoundedCornerShape(16.dp))
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(CadencePurple.copy(alpha = 0.1f), shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CadencePurple,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = CadenceTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = CadenceTextSecondary
                )
            }
            trailingContent()
        }
        Spacer(modifier = Modifier.height(24.dp))
        content()
    }
}

@Composable
fun SettingsToggleRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color = CadenceTextSecondary,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = CadenceTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = CadenceTextSecondary
                )
            }
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = CadencePurple,
                uncheckedThumbColor = CadenceTextSecondary,
                uncheckedTrackColor = Color(0xFFE5E7EB)
            )
        )
    }
}

@Composable
fun SettingsInputRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CadenceTextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = CadenceTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = CadenceTextSecondary
                )
            }
        }
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.width(200.dp),
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CadencePurple,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = CadenceTextPrimary,
                unfocusedTextColor = CadenceTextPrimary,
                cursorColor = CadencePurple
            )
        )
    }
}
