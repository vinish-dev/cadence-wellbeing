package com.vinish.cadence.tracking

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

@Serializable
data class SettingsState(
    val showActivityOverviewChart: Boolean = true,
    val showSessionActiveCard: Boolean = false,
    val showDonutChart: Boolean = false,
    val breakTimerMinutes: Int = 45,
    val userName: String = "Cadence User"
)

object SettingsManager {
    private val _settings = MutableStateFlow(StorageManager.loadSettings() ?: SettingsState())
    val settings = _settings.asStateFlow()

    fun toggleActivityOverviewChart(show: Boolean) {
        _settings.value = _settings.value.copy(showActivityOverviewChart = show)
        StorageManager.saveSettings(_settings.value)
    }

    fun toggleSessionActiveCard(show: Boolean) {
        _settings.value = _settings.value.copy(showSessionActiveCard = show)
        StorageManager.saveSettings(_settings.value)
    }

    fun toggleDonutChart(show: Boolean) {
        _settings.value = _settings.value.copy(showDonutChart = show)
        StorageManager.saveSettings(_settings.value)
    }

    fun updateBreakTimer(minutes: Int) {
        _settings.value = _settings.value.copy(breakTimerMinutes = minutes)
        StorageManager.saveSettings(_settings.value)
    }

    fun updateUserName(name: String) {
        _settings.value = _settings.value.copy(userName = name)
        StorageManager.saveSettings(_settings.value)
    }
}
