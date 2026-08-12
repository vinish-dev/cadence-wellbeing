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
        }
    }
}
