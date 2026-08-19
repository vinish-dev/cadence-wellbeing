package com.vinish.cadence.tracking

import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.Kernel32Util
import com.sun.jna.platform.win32.WinReg

object StartupManager {
    private const val RUN_KEY = "Software\\Microsoft\\Windows\\CurrentVersion\\Run"
    private const val APP_NAME = "Cadence"

    fun setRunAtStartup(enabled: Boolean) {
        try {
            var exePath = System.getProperty("jpackage.app-path")
            if (exePath == null) {
                exePath = ProcessHandle.current().info().command().orElse("")
            }
            
            // If running via gradle/IDE, the exe path might be java.exe. 
            // We should only set startup if it's the actual packaged app.
            if (exePath.endsWith("java.exe", ignoreCase = true) || exePath.endsWith("javaw.exe", ignoreCase = true)) {
                println("Cannot set run at startup: Running via IDE/Gradle ($exePath)")
                return
            }

            if (enabled) {
                // Ensure path is quoted in case of spaces and append the startup flag
                val command = "\"$exePath\" --startup"
                Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, RUN_KEY, APP_NAME, command)
            } else {
                if (Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, RUN_KEY, APP_NAME)) {
                    Advapi32Util.registryDeleteValue(WinReg.HKEY_CURRENT_USER, RUN_KEY, APP_NAME)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isRunAtStartupEnabled(): Boolean {
        return try {
            Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, RUN_KEY, APP_NAME)
        } catch (e: Exception) {
            false
        }
    }
}
