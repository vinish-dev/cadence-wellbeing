package com.vinish.cadence

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Cadence",
        resizable = true,
    ) {
        window.minimumSize = java.awt.Dimension(1080, 760)
        App()
    }
}
