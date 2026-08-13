package com.vinish.cadence

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    // Add shutdown hook to save active session
    Runtime.getRuntime().addShutdownHook(Thread {
        com.vinish.cadence.tracking.SessionManager.currentSession.value?.let { session ->
            // End the last segment
            session.segments.lastOrNull()?.endTime = java.time.Instant.now()
            com.vinish.cadence.tracking.StorageManager.saveActiveSession(session)
        }
    })

    Window(
        onCloseRequest = ::exitApplication,
        title = "Cadence",
        resizable = true,
    ) {
        window.minimumSize = java.awt.Dimension(1080, 760)
        App()
    }
}
