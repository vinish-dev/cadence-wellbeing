package com.vinish.cadence

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.vinish.cadence.tracking.AppTracker
import com.vinish.cadence.tracking.KeyboardTracker
import kotlin.concurrent.thread

fun main() = application {

    thread(start = true, isDaemon = true) {
        KeyboardTracker.start()
    }

    thread(start = true, isDaemon = true) {
        AppTracker.start()
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Cadence"
    ) {
        App()
    }
}