package com.vinish.cadence.tracking

import com.vinish.cadence.tracking.models.Session
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object StorageManager {
    private val appDataDir: File by lazy {
        val appData = System.getenv("APPDATA") ?: System.getProperty("user.home")
        File(appData, "Cadence").apply { mkdirs() }
    }

    private val sessionsDir: File by lazy {
        File(appDataDir, "sessions").apply { mkdirs() }
    }

    private val settingsFile = File(appDataDir, "settings.json")
    private val activeSessionFile = File(appDataDir, "active_session.json")

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault())

    fun saveSettings(state: SettingsState) {
        try {
            settingsFile.writeText(json.encodeToString(state))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadSettings(): SettingsState? {
        if (!settingsFile.exists()) return null
        return try {
            json.decodeFromString<SettingsState>(settingsFile.readText())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveActiveSession(session: Session?) {
        try {
            if (session == null) {
                if (activeSessionFile.exists()) activeSessionFile.delete()
            } else {
                activeSessionFile.writeText(json.encodeToString(session))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadActiveSession(): Session? {
        if (!activeSessionFile.exists()) return null
        return try {
            json.decodeFromString<Session>(activeSessionFile.readText())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getSessionFileForDate(dateStr: String): File {
        return File(sessionsDir, "sessions-$dateStr.json")
    }

    fun saveSession(session: Session) {
        try {
            val dateStr = dateFormatter.format(session.startTime)
            val file = getSessionFileForDate(dateStr)
            
            val existingSessions = if (file.exists()) {
                try {
                    json.decodeFromString<List<Session>>(file.readText())
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }

            // Add the new session, deduplicating by start time
            val allSessions = (existingSessions + session)
                .associateBy { it.startTime }
                .values
                .sortedByDescending { it.startTime }
            
            file.writeText(json.encodeToString(allSessions))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadTodaySessions(): List<Session> {
        val todayStr = dateFormatter.format(Instant.now())
        val file = getSessionFileForDate(todayStr)
        if (!file.exists()) return emptyList()
        return try {
            json.decodeFromString<List<Session>>(file.readText())
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun clearTodayData() {
        val todayStr = dateFormatter.format(Instant.now())
        val file = getSessionFileForDate(todayStr)
        if (file.exists()) {
            file.delete()
        }
        if (activeSessionFile.exists()) {
            activeSessionFile.delete()
        }
    }
}
