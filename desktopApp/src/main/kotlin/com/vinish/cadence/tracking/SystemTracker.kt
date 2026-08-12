package com.vinish.cadence.tracking

import com.sun.jna.Structure
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.platform.win32.Wtsapi32
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

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

enum class SystemEvent {
    Locked,
    Unlocked,
    Suspended,
    Resumed,
    Shutdown
}

object SystemTracker {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val _systemEvents = MutableSharedFlow<SystemEvent>(extraBufferCapacity = 10)
    val systemEvents = _systemEvents.asSharedFlow()

    private const val WM_WTSSESSION_CHANGE = 0x02B1
    private const val WTS_SESSION_LOCK = 0x7
    private const val WTS_SESSION_UNLOCK = 0x8
    
    private const val WM_POWERBROADCAST = 0x021B
    private const val PBT_APMSUSPEND = 0x0004
    private const val PBT_APMRESUMESUSPEND = 0x0007
    private const val PBT_APMRESUMEAUTOMATIC = 0x0012

    private const val WM_QUERYENDSESSION = 0x0011
    private const val WM_ENDSESSION = 0x0016

    private var wndProc: WinUser.WindowProc? = null

    fun start() {
        thread(start = true, isDaemon = true, name = "SystemTrackerMessageLoop") {
            val className = "CadenceSystemTrackerClass"
            
            // We must keep a strong reference to the WindowProc callback to prevent GC
            wndProc = object : WinUser.WindowProc {
                override fun callback(
                    hwnd: WinDef.HWND,
                    uMsg: Int,
                    wParam: WinDef.WPARAM,
                    lParam: WinDef.LPARAM
                ): WinDef.LRESULT {
                    when (uMsg) {
                        WM_WTSSESSION_CHANGE -> {
                            when (wParam.toInt()) {
                                WTS_SESSION_LOCK -> emitEvent(SystemEvent.Locked)
                                WTS_SESSION_UNLOCK -> emitEvent(SystemEvent.Unlocked)
                            }
                        }
                        WM_POWERBROADCAST -> {
                            when (wParam.toInt()) {
                                PBT_APMSUSPEND -> emitEvent(SystemEvent.Suspended)
                                PBT_APMRESUMESUSPEND, PBT_APMRESUMEAUTOMATIC -> emitEvent(SystemEvent.Resumed)
                            }
                        }
                        WM_QUERYENDSESSION, WM_ENDSESSION -> {
                            emitEvent(SystemEvent.Shutdown)
                        }
                    }
                    return User32.INSTANCE.DefWindowProc(hwnd, uMsg, wParam, lParam)
                }
            }

            val wClass = WinUser.WNDCLASSEX()
            wClass.cbSize = wClass.size()
            wClass.lpszClassName = className
            wClass.lpfnWndProc = wndProc
            
            User32.INSTANCE.RegisterClassEx(wClass)
            
            val hwnd = User32.INSTANCE.CreateWindowEx(
                0, className, "CadenceHiddenWindow",
                0, 0, 0, 0, 0,
                null, null, null, null
            )
            
            if (hwnd != null) {
                // Register for session lock/unlock notifications (NOTIFY_FOR_THIS_SESSION = 0)
                Wtsapi32.INSTANCE.WTSRegisterSessionNotification(hwnd, 0)
            }

            val msg = WinUser.MSG()
            while (User32.INSTANCE.GetMessage(msg, null, 0, 0) > 0) {
                User32.INSTANCE.TranslateMessage(msg)
                User32.INSTANCE.DispatchMessage(msg)
            }
        }
    }

    private fun emitEvent(event: SystemEvent) {
        scope.launch {
            _systemEvents.emit(event)
        }
    }

    /**
     * Returns the idle time in milliseconds.
     */
    fun getIdleTimeMillis(): Long {
        val lastInputInfo = LASTINPUTINFO()
        if (User32Ex.INSTANCE.GetLastInputInfo(lastInputInfo)) {
            val lastInputTick = lastInputInfo.dwTime.toLong() and 0xFFFFFFFFL
            val currentTick = Kernel32.INSTANCE.GetTickCount().toLong() and 0xFFFFFFFFL
            
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
