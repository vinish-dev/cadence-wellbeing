package com.vinish.cadence.tracking

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsState(
    val showActivityOverviewChart: Boolean = true
)

object SettingsManager {
    private val _settings = MutableStateFlow(SettingsState())
    val settings = _settings.asStateFlow()

    fun toggleActivityOverviewChart(show: Boolean) {
        _settings.value = _settings.value.copy(showActivityOverviewChart = show)
    }
}
