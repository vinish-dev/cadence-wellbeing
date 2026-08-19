package com.vinish.cadence.tracking

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Duration
import java.time.Instant

data class FocusState(
    val focusStartTime: Instant = Instant.now(),
    val snoozeUntil: Instant? = null,
    val isBreakDetected: Boolean = false,
    val hasSentNotification: Boolean = false,
    val hasSentSmartBreakNotification: Boolean = false
) {
    val currentFocusMinutes: Int
        get() {
            if (isBreakDetected) return 0
            return (Duration.between(focusStartTime, Instant.now()).seconds / 60).toInt()
        }
}

object FocusManager {
    private val _state = MutableStateFlow(FocusState())
    val state = _state.asStateFlow()
    
    private var lastPeakIdleSeconds = 0L

    fun checkIdle(idleSeconds: Long) {
        if (idleSeconds > 0) {
            lastPeakIdleSeconds = idleSeconds
        }
        
        val current = _state.value
        val focusMins = (Duration.between(current.focusStartTime, Instant.now()).seconds / 60).toInt()
        val threshold = SettingsManager.settings.value.breakTimerMinutes
        val isSnoozed = current.snoozeUntil != null && Instant.now().isBefore(current.snoozeUntil)

        if (idleSeconds == 0L) {
            // Activity resumed
            if (current.isBreakDetected) {
                // They were on a detected break, now they are back. Reset focus.
                SessionManager.onSmartBreakEnded(lastPeakIdleSeconds)
                _state.value = FocusState(focusStartTime = Instant.now())
                lastPeakIdleSeconds = 0L
                return
            }
            lastPeakIdleSeconds = 0L
        } else {
            // Check if we should detect a break
            if (focusMins >= threshold && !isSnoozed && idleSeconds >= 180) { // 3 minutes = 180 seconds
                if (!current.isBreakDetected) {
                    _state.value = current.copy(isBreakDetected = true, hasSentSmartBreakNotification = true)
                    if (SettingsManager.settings.value.smartBreakNotificationsEnabled) {
                        NotificationManager.sendNotification("Break detected", "You've been away for 3 minutes. Your focus timer has been paused.")
                    }
                    return
                }
            }
        }
        
        // Trigger notification
        if (focusMins >= threshold && !isSnoozed && !current.hasSentNotification) {
            _state.value = _state.value.copy(hasSentNotification = true)
            if (SettingsManager.settings.value.breakRemindersEnabled) {
                NotificationManager.sendNotification("Time for a break!", "You've been focused for $focusMins minutes. Step away to recharge.")
            }
        }
    }

    fun markBreakDetected() {
        if (!_state.value.isBreakDetected) {
            _state.value = _state.value.copy(isBreakDetected = true)
        }
    }

    fun snoozeBreak() {
        _state.value = _state.value.copy(
            snoozeUntil = Instant.now().plusSeconds(15 * 60),
            hasSentNotification = false
        )
    }
}
