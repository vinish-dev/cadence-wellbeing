package com.vinish.cadence.tracking

import com.vinish.cadence.tracking.models.Segment
import com.vinish.cadence.tracking.models.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant

object SessionManager {
    private val scope = CoroutineScope(Dispatchers.Default)
    
    private val _sessions = MutableStateFlow<List<Session>>(emptyList())
    val sessions = _sessions.asStateFlow()

    private val _currentSession = MutableStateFlow<Session?>(null)
    val currentSession = _currentSession.asStateFlow()

    init {
        _sessions.value = StorageManager.loadTodaySessions()
        
        val loadedSession = StorageManager.loadActiveSession()
        if (loadedSession != null) {
            val lastActivity = loadedSession.segments.lastOrNull()?.endTime 
                ?: loadedSession.segments.lastOrNull()?.startTime 
                ?: loadedSession.startTime
            
            val idleDuration = java.time.Duration.between(lastActivity, Instant.now()).seconds
            if (idleDuration > 900) { // 15 minutes gap means the session is definitively over
                loadedSession.endTime = lastActivity
                StorageManager.saveSession(loadedSession)
                StorageManager.saveActiveSession(null)
                _currentSession.value = null
            } else {
                _currentSession.value = loadedSession
            }
        } else {
            _currentSession.value = null
        }
    }

    fun start() {
        scope.launch {
            SystemTracker.systemEvents.collect { event ->
                when (event) {
                    SystemEvent.Locked, SystemEvent.Suspended, SystemEvent.Shutdown -> {
                        endCurrentSession()
                    }
                    SystemEvent.Unlocked, SystemEvent.Resumed -> {
                        // The next app change will naturally start a new session
                    }
                }
            }
        }
    }

    fun onAppChanged(appName: String, windowTitle: String, timestamp: Instant) {
        val current = _currentSession.value
        
        if (current == null) {
            // Start a new session
            val newSession = Session(startTime = timestamp).apply {
                segments.add(Segment(appName, windowTitle, timestamp))
            }
            _currentSession.value = newSession
        } else {
            // End the current segment
            current.segments.lastOrNull()?.endTime = timestamp
            
            // Add a new segment
            current.segments.add(Segment(appName, windowTitle, timestamp))
            
            // Trigger flow update with a new list reference
            _currentSession.value = current.copy(segments = current.segments.toMutableList())
        }
        StorageManager.saveActiveSession(_currentSession.value)
    }

    fun incrementKeysTyped() {
        _currentSession.value?.keysTyped = (_currentSession.value?.keysTyped ?: 0) + 1
    }

    fun incrementMouseClicks() {
        _currentSession.value?.mouseClicks = (_currentSession.value?.mouseClicks ?: 0) + 1
    }

    fun onIdleTimeout(timestamp: Instant) {
        endCurrentSession(timestamp)
    }

    private fun endCurrentSession(timestamp: Instant = Instant.now()) {
        FocusManager.markBreakDetected()
        val current = _currentSession.value
        if (current != null) {
            // End the current segment and session
            current.segments.lastOrNull()?.endTime = timestamp
            current.endTime = timestamp
            
            // Move session to past sessions list (inserted at index 0 for newest-first)
            _sessions.value = listOf(current) + _sessions.value
            _currentSession.value = null
            
            StorageManager.saveSession(current)
            StorageManager.saveActiveSession(null)
        }
    }
}
