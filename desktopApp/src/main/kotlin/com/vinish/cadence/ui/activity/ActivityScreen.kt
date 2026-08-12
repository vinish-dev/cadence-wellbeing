package com.vinish.cadence.ui.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vinish.cadence.ui.activity.components.SessionCard
import com.vinish.cadence.ui.theme.CadenceTextSecondary

@Composable
fun ActivityScreen(
    sessions: List<SessionSummaryData>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        if (sessions.isEmpty()) {
            Text(
                text = "No sessions recorded yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = CadenceTextSecondary,
                modifier = Modifier.padding(18.dp)
            )
        } else {
            sessions.forEach { session ->
                SessionCard(
                    session = session,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
