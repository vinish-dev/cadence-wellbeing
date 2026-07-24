package com.vinish.cadence.tracking

import com.vinish.cadence.tracking.models.Segment
import com.vinish.cadence.tracking.models.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant

object SessionManager {
    private val _sessions = MutableStateFlow<List<Session>>(emptyList())
    val sessions = _sessions.asStateFlow()

    private val _currentSession = MutableStateFlow<Session?>(null)
    val currentSession = _currentSession.asStateFlow()

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
    }

    fun onIdleTimeout(timestamp: Instant) {
        val current = _currentSession.value
        if (current != null) {
            // End the current segment and session
            current.segments.lastOrNull()?.endTime = timestamp
            current.endTime = timestamp
            
            // Move session to past sessions list
            _sessions.value = _sessions.value + current
            _currentSession.value = null
        }
    }
}
