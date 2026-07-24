package com.vinish.cadence.tracking

import com.sun.jna.Structure
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef

// JNA structure for GetLastInputInfo
class LASTINPUTINFO : Structure() {
    @JvmField var cbSize: Int = 0
    @JvmField var dwTime: Int = 0

    init {
        cbSize = size()
    }

    override fun getFieldOrder(): List<String> {
        return listOf("cbSize", "dwTime")
    }
}

interface User32Ex : User32 {
    fun GetLastInputInfo(result: LASTINPUTINFO): Boolean

    companion object {
        val INSTANCE: User32Ex = com.sun.jna.Native.load("user32", User32Ex::class.java)
    }
}

object SystemTracker {
    /**
     * Returns the idle time in milliseconds.
     * Calculated as: Current TickCount - TickCount of Last Input
     */
    fun getIdleTimeMillis(): Long {
        val lastInputInfo = LASTINPUTINFO()
        if (User32Ex.INSTANCE.GetLastInputInfo(lastInputInfo)) {
            val lastInputTick = lastInputInfo.dwTime.toLong() and 0xFFFFFFFFL
            val currentTick = Kernel32.INSTANCE.GetTickCount().toLong() and 0xFFFFFFFFL
            
            // Handle tick count wrap-around (happens every 49.7 days)
            var idleTime = currentTick - lastInputTick
            if (idleTime < 0) {
                idleTime += 0x100000000L
            }
            return idleTime
        }
        return 0L
    }

    /**
     * Returns the idle time in seconds.
     */
    fun getIdleTimeSeconds(): Long {
        return getIdleTimeMillis() / 1000L
    }
}
