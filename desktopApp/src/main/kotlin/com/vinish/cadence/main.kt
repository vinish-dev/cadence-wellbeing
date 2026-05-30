package com.vinish.cadence

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.vinish.cadence.tracking.KeyboardTracker
import kotlin.concurrent.thread

fun main() = application {

    thread(start = true, isDaemon = true) {
        KeyboardTracker.start()
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Cadence"
    ) {
        App()
    }
}