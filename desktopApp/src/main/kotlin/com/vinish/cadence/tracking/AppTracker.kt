package com.vinish.cadence.tracking

import com.sun.jna.platform.win32.User32
import com.sun.jna.ptr.IntByReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.time.Instant
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
    private const val IDLE_TIMEOUT_SECONDS = 300L

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
        var lastAppName: String? = null
        var lastWindowTitle: String? = null
        var isIdle = false

        while (running) {
            try {
                val idleSeconds = SystemTracker.getIdleTimeSeconds()

                if (idleSeconds > IDLE_TIMEOUT_SECONDS) {
                    if (!isIdle) {
                        isIdle = true
                        // Calculate the timestamp when the idle period actually started
                        val idleStart = Instant.now().minusSeconds(idleSeconds)
                        SessionManager.onIdleTimeout(idleStart)
                        lastAppName = null
                        lastWindowTitle = null
                    }
                } else {
                    if (isIdle) {
                        // User came back from idle
                        isIdle = false
                    }

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

                        // Get app name
                        val exeName = ProcessHandle.of(pid.toLong())
                            .flatMap { it.info().command() }
                            .map { File(it).name }
                            .orElse("Unknown")

                        val friendlyAppName = getFriendlyAppName(exeName)

                        // Check if app or title changed
                        if (friendlyAppName != lastAppName || windowTitle != lastWindowTitle) {
                            SessionManager.onAppChanged(friendlyAppName, windowTitle, Instant.now())
                            lastAppName = friendlyAppName
                            lastWindowTitle = windowTitle
                        }
                    } else {
                        // No active window
                        if (lastAppName != "None") {
                            SessionManager.onAppChanged("None", "", Instant.now())
                            lastAppName = "None"
                            lastWindowTitle = ""
                        }
                    }
                }

                // Update TrackerState for the UI every second
                val session = SessionManager.currentSession.value
                if (session != null) {
                    val segments = session.segments
                    val lastSegment = segments.lastOrNull()

                    // Aggregate usage for TrackerState.appUsages
                    val usagesMap = mutableMapOf<String, Long>()
                    for (seg in segments) {
                        usagesMap[seg.appName] = (usagesMap[seg.appName] ?: 0L) + seg.durationSeconds
                    }
                    val usagesList = usagesMap.map { (name, duration) ->
                        AppUsage(name, duration, formatDuration(duration))
                    }.sortedByDescending { it.durationSeconds }

                    _state.value = TrackerState(
                        activeApp = lastSegment?.appName ?: "None",
                        activeWindowTitle = lastSegment?.windowTitle ?: "",
                        appUsages = usagesList
                    )
                } else {
                    _state.value = TrackerState(
                        activeApp = "None",
                        activeWindowTitle = "",
                        appUsages = emptyList()
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
