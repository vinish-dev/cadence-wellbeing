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
    val userName: String = "Cadence User",
    val isKeyboardTrackingEnabled: Boolean = true,
    val isMouseTrackingEnabled: Boolean = true,
    val isActivityDetectionEnabled: Boolean = true,
    val runAtStartup: Boolean = true,
    val breakRemindersEnabled: Boolean = true,
    val notificationSoundEnabled: Boolean = true,
    val smartBreakNotificationsEnabled: Boolean = true
)

object SettingsManager {
    private val _settings = MutableStateFlow(SettingsState())
    val settings = _settings.asStateFlow()

    init {
        val loaded = StorageManager.loadSettings()
        val isFirstLaunch = loaded == null
        val currentSettings = loaded ?: SettingsState()
        
        val actualStartupState = StartupManager.isRunAtStartupEnabled()
        
        if (isFirstLaunch && currentSettings.runAtStartup) {
            // First launch, apply the default true setting to the OS
            StartupManager.setRunAtStartup(true)
            _settings.value = currentSettings
            StorageManager.saveSettings(currentSettings)
        } else {
            // Not first launch. OS is the source of truth for startup state.
            if (currentSettings.runAtStartup != actualStartupState) {
                val synced = currentSettings.copy(runAtStartup = actualStartupState)
                _settings.value = synced
                StorageManager.saveSettings(synced)
            } else {
                _settings.value = currentSettings
            }
        }
    }

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

    fun toggleKeyboardTracking(enabled: Boolean) {
        _settings.value = _settings.value.copy(isKeyboardTrackingEnabled = enabled)
        StorageManager.saveSettings(_settings.value)
    }

    fun toggleMouseTracking(enabled: Boolean) {
        _settings.value = _settings.value.copy(isMouseTrackingEnabled = enabled)
        StorageManager.saveSettings(_settings.value)
    }

    fun toggleActivityDetection(enabled: Boolean) {
        _settings.value = _settings.value.copy(isActivityDetectionEnabled = enabled)
        StorageManager.saveSettings(_settings.value)
    }

    fun toggleRunAtStartup(enabled: Boolean) {
        _settings.value = _settings.value.copy(runAtStartup = enabled)
        StorageManager.saveSettings(_settings.value)
        StartupManager.setRunAtStartup(enabled)
    }

    fun toggleBreakReminders(enabled: Boolean) {
        _settings.value = _settings.value.copy(breakRemindersEnabled = enabled)
        StorageManager.saveSettings(_settings.value)
    }

    fun toggleNotificationSound(enabled: Boolean) {
        _settings.value = _settings.value.copy(notificationSoundEnabled = enabled)
        StorageManager.saveSettings(_settings.value)
    }

    fun toggleSmartBreakNotifications(enabled: Boolean) {
        _settings.value = _settings.value.copy(smartBreakNotificationsEnabled = enabled)
        StorageManager.saveSettings(_settings.value)
    }
}
