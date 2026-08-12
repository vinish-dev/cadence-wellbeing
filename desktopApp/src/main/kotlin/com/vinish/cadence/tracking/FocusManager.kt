package com.vinish.cadence.tracking

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Duration
import java.time.Instant

data class FocusState(
    val focusStartTime: Instant = Instant.now(),
    val snoozeUntil: Instant? = null,
    val isBreakDetected: Boolean = false
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

    fun checkIdle(idleSeconds: Long) {
        val current = _state.value
        if (idleSeconds == 0L) {
            // Activity resumed
            if (current.isBreakDetected) {
                // They were on a detected break, now they are back. Reset focus.
                _state.value = FocusState(focusStartTime = Instant.now())
            }
        } else {
            // Check if we should detect a break
            val focusMins = (Duration.between(current.focusStartTime, Instant.now()).seconds / 60).toInt()
            val threshold = SettingsManager.settings.value.breakTimerMinutes
            if (focusMins >= threshold && idleSeconds >= 180) { // 3 minutes = 180 seconds
                if (!current.isBreakDetected) {
                    _state.value = current.copy(isBreakDetected = true)
                }
            }
        }
    }

    fun resetFocus() {
        _state.value = FocusState(focusStartTime = Instant.now())
    }

    fun snoozeBreak() {
        _state.value = _state.value.copy(snoozeUntil = Instant.now().plusSeconds(15 * 60))
    }
}
