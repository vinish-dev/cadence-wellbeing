package com.vinish.cadence.tracking

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsState(
    val showActivityOverviewChart: Boolean = true,
    val showSessionActiveCard: Boolean = false,
    val breakTimerMinutes: Int = 45
)

object SettingsManager {
    private val _settings = MutableStateFlow(SettingsState())
    val settings = _settings.asStateFlow()

    fun toggleActivityOverviewChart(show: Boolean) {
        _settings.value = _settings.value.copy(showActivityOverviewChart = show)
    }

    fun toggleSessionActiveCard(show: Boolean) {
        _settings.value = _settings.value.copy(showSessionActiveCard = show)
    }

    fun updateBreakTimer(minutes: Int) {
        _settings.value = _settings.value.copy(breakTimerMinutes = minutes)
    }
}
