package com.vinish.cadence

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vinish.cadence.tracking.AppTracker
import com.vinish.cadence.tracking.KeyboardTracker

@Composable
fun App() {

    val typingCount by KeyboardTracker.typingCount.collectAsState()
    val trackerState by AppTracker.state.collectAsState()

    MaterialTheme {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource("images/img6.jpg"),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            // Semi-transparent overlay for better contrast
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Cadence",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Text(
                    text = "Total Keys: $typingCount",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Current Active App Section
                ActiveAppSection(trackerState.activeApp, trackerState.activeWindowTitle)

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Usage History",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Text(
                        text = "${trackerState.appUsages.size} apps tracked",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable List of App Usage
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(trackerState.appUsages) { usage ->
                        UsageRow(usage.appName, usage.formattedDuration)
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveAppSection(appName: String, windowTitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Active Now",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Cyan.copy(alpha = 0.8f),
                letterSpacing = 1.2.sp
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = appName,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            if (windowTitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = windowTitle,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun UsageRow(name: String, duration: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.08f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            
            Box(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = duration,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
