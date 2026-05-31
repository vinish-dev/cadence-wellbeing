package com.vinish.cadence

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vinish.cadence.tracking.KeyboardTracker

@Composable
fun App() {

    val typingCount by KeyboardTracker.typingCount.collectAsState()

    MaterialTheme {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource("images/img6.jpg"),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Cadence",
                    fontSize = 32.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Typing Count: $typingCount",
                    fontSize = 20.sp
                )
            }
        }
    }
}