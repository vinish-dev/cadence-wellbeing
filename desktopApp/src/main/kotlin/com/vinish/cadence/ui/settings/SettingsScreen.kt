package com.vinish.cadence.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    Column(modifier = modifier.border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(20.dp))) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(24.dp)
        ) {
            Text(
                text = "Dashboard Preferences",
                style = MaterialTheme.typography.titleMedium,
                color = CadenceTextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Show Activity Overview Line Chart",
                        style = MaterialTheme.typography.bodyLarge,
                        color = CadenceTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Displays a timeline chart of your focus sessions on the dashboard.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CadenceTextSecondary
                    )
                }
                
                Switch(
                    checked = state.showActivityOverviewChart,
                    onCheckedChange = onToggleActivityChart,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = CadencePurple,
                        uncheckedThumbColor = CadenceTextSecondary,
                        uncheckedTrackColor = Color(0xFFE5E7EB)
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Show Session Active Card",
                        style = MaterialTheme.typography.bodyLarge,
                        color = CadenceTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Displays the current session tracking status on the dashboard.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CadenceTextSecondary
                    )
                }
                
                Switch(
                    checked = state.showSessionActiveCard,
                    onCheckedChange = onToggleSessionActiveCard,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = CadencePurple,
                        uncheckedThumbColor = CadenceTextSecondary,
                        uncheckedTrackColor = Color(0xFFE5E7EB)
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Show Top Apps Donut Chart",
                        style = MaterialTheme.typography.bodyLarge,
                        color = CadenceTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Displays a visual breakdown of your top applications.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CadenceTextSecondary
                    )
                }
                
                Switch(
                    checked = state.showDonutChart,
                    onCheckedChange = onToggleDonutChart,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = CadencePurple,
                        uncheckedThumbColor = CadenceTextSecondary,
                        uncheckedTrackColor = Color(0xFFE5E7EB)
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Display Name",
                    style = MaterialTheme.typography.bodyLarge,
                    color = CadenceTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "How you want to be greeted on the dashboard.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CadenceTextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                androidx.compose.material3.OutlinedTextField(
                    value = state.userName,
                    onValueChange = { com.vinish.cadence.tracking.SettingsManager.updateUserName(it) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    singleLine = true,
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CadencePurple,
                        unfocusedBorderColor = Color(0xFFE5E7EB),
                        focusedTextColor = CadenceTextPrimary,
                        unfocusedTextColor = CadenceTextPrimary,
                        cursorColor = CadencePurple
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Break Timer",
                    style = MaterialTheme.typography.bodyLarge,
                    color = CadenceTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Set how often you'd like to be reminded to take a break. Current: ${state.breakTimerMinutes} minutes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CadenceTextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                androidx.compose.material3.Slider(
                    value = state.breakTimerMinutes.toFloat(),
                    onValueChange = { com.vinish.cadence.tracking.SettingsManager.updateBreakTimer(it.toInt()) },
                    valueRange = 15f..120f,
                    steps = 20, // 105 range / 5 = 21 steps? 20 intermediate steps
                    colors = androidx.compose.material3.SliderDefaults.colors(
                        thumbColor = CadencePurple,
                        activeTrackColor = CadencePurple,
                        inactiveTrackColor = Color(0xFFE5E7EB)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("15m", style = MaterialTheme.typography.bodySmall, color = CadenceTextSecondary)
                    Text("120m", style = MaterialTheme.typography.bodySmall, color = CadenceTextSecondary)
                }
            }
        }
    }
}
