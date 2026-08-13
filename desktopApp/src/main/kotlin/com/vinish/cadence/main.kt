package com.vinish.cadence

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    // Add shutdown hook to save active session
    Runtime.getRuntime().addShutdownHook(Thread {
        com.vinish.cadence.tracking.SessionManager.currentSession.value?.let { session ->
            // End the last segment
            session.segments.lastOrNull()?.endTime = java.time.Instant.now()
            com.vinish.cadence.tracking.StorageManager.saveActiveSession(session)
        }
    })

    // Start trackers globally so they run independently of the UI window
    com.vinish.cadence.tracking.SystemTracker.start()
    com.vinish.cadence.tracking.SessionManager.start()
    com.vinish.cadence.tracking.AppTracker.start()
    com.vinish.cadence.tracking.KeyboardTracker.start()

    application {
        var isWindowVisible by remember { mutableStateOf(true) }

        val trayIcon = painterResource("images/winter-pear.png")
//        val trayIcon = painterResource("images/sakura.png")
//        val trayIcon = painterResource("images/pink-cosmos.png")

        Tray(
            icon = trayIcon,
            tooltip = "Cadence",
            onAction = { isWindowVisible = true },
            menu = {
                Item(
                    "Show Dashboard",
                    onClick = { isWindowVisible = true }
                )
                Item(
                    "Exit Cadence",
                    onClick = { exitApplication() }
                )
            }
        )

        if (isWindowVisible) {
            Window(
                onCloseRequest = { isWindowVisible = false },
                title = "Cadence",
                resizable = true,
                icon = trayIcon,
            ) {
                window.minimumSize = java.awt.Dimension(1080, 760)
                App()
            }
        }
    }
}
