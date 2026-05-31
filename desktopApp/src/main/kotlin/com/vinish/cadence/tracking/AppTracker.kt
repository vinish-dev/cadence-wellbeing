package com.vinish.cadence.tracking

import com.sun.jna.platform.win32.User32
import com.sun.jna.ptr.IntByReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import kotlin.concurrent.thread

data class AppUsage(
    val appName: String,
    val durationSeconds: Long,
    val formattedDuration: String
)

data class TrackerState(
    val activeApp: String = "None",
    val activeWindowTitle: String = "",
    val appUsages: List<AppUsage> = emptyList()
)

object AppTracker {
    private val _state = MutableStateFlow(TrackerState())
    val state = _state.asStateFlow()

    private var running = false

    fun start() {
        synchronized(this) {
            if (running) return
            running = true
        }
        thread(start = true, isDaemon = true, name = "AppTrackerThread") {
            trackLoop()
        }
    }

    private fun trackLoop() {
        val usagesMap = mutableMapOf<String, Long>() // appName -> durationSeconds
        var lastAppName: String? = null
        var lastTime = System.currentTimeMillis()

        while (running) {
            try {
                val hwnd = User32.INSTANCE.GetForegroundWindow()
                if (hwnd != null) {
                    // Get window text
                    val titleLength = User32.INSTANCE.GetWindowTextLength(hwnd)
                    val windowTitle = if (titleLength > 0) {
                        val buffer = CharArray(titleLength + 1)
                        User32.INSTANCE.GetWindowText(hwnd, buffer, buffer.size)
                        String(buffer, 0, titleLength)
                    } else {
                        ""
                    }

                    // Get PID
                    val processId = IntByReference()
                    User32.INSTANCE.GetWindowThreadProcessId(hwnd, processId)
                    val pid = processId.value

                    // Get app name using Java 9+ ProcessHandle
                    val exeName = ProcessHandle.of(pid.toLong())
                        .flatMap { it.info().command() }
                        .map { File(it).name }
                        .orElse("Unknown")

                    val friendlyAppName = getFriendlyAppName(exeName)

                    // Calculate elapsed time in seconds
                    val currentTime = System.currentTimeMillis()
                    val elapsedSeconds = (currentTime - lastTime) / 1000

                    if (elapsedSeconds > 0) {
                        lastTime = currentTime
                        if (lastAppName != null) {
                            usagesMap[lastAppName] = (usagesMap[lastAppName] ?: 0L) + elapsedSeconds
                        }
                    }

                    // Update currently active app
                    lastAppName = friendlyAppName

                    val usagesList = usagesMap.map { (name, duration) ->
                        AppUsage(name, duration, formatDuration(duration))
                    }.sortedByDescending { it.durationSeconds }

                    _state.value = TrackerState(
                        activeApp = friendlyAppName,
                        activeWindowTitle = windowTitle,
                        appUsages = usagesList
                    )
                } else {
                    // No active window
                    val currentTime = System.currentTimeMillis()
                    val elapsedSeconds = (currentTime - lastTime) / 1000

                    if (elapsedSeconds > 0) {
                        lastTime = currentTime
                        if (lastAppName != null) {
                            usagesMap[lastAppName] = (usagesMap[lastAppName] ?: 0L) + elapsedSeconds
                        }
                    }
                    lastAppName = null

                    val usagesList = usagesMap.map { (name, duration) ->
                        AppUsage(name, duration, formatDuration(duration))
                    }.sortedByDescending { it.durationSeconds }

                    _state.value = TrackerState(
                        activeApp = "None",
                        activeWindowTitle = "",
                        appUsages = usagesList
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                break
            }
        }
    }

    private fun getFriendlyAppName(exeName: String): String {
        val nameWithoutExt = exeName.substringBeforeLast('.', exeName)
        return when (nameWithoutExt.lowercase()) {
            "code" -> "VS Code"
            "chrome" -> "Chrome"
            "discord" -> "Discord"
            "idea64" -> "IntelliJ IDEA"
            "msedge" -> "Edge"
            "firefox" -> "Firefox"
            "explorer" -> "Windows Explorer"
            "cmd" -> "Command Prompt"
            "powershell" -> "PowerShell"
            "notepad" -> "Notepad"
            "slack" -> "Slack"
            "spotify" -> "Spotify"
            "cadence" -> "Cadence"
            else -> nameWithoutExt.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }

    fun formatDuration(seconds: Long): String {
        if (seconds < 60) {
            return "${seconds}s"
        }
        val minutes = seconds / 60
        if (minutes < 60) {
            return "${minutes}m"
        }
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return "${hours}h ${remainingMinutes}m"
    }
}
