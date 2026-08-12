package com.vinish.cadence.tracking

object AppFilter {
    private val IGNORED_PROCESSES = setOf(
        "lockapp.exe",
        "searchhost.exe",
        "startmenuexperiencehost.exe",
        "shellexperiencehost.exe",
        "textinputhost.exe",
        "applicationframehost.exe",
        "systemsettings.exe",
        "idle"
    )

    fun shouldIgnore(exeName: String?): Boolean {
        if (exeName.isNullOrBlank()) return true
        if (exeName.equals("Unknown", ignoreCase = true)) return true
        
        val normalized = exeName.lowercase()
        return IGNORED_PROCESSES.contains(normalized)
    }
}
