package com.vinish.cadence.tracking

import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser.HHOOK
import com.sun.jna.platform.win32.WinUser.HOOKPROC
import com.sun.jna.platform.win32.WinUser.MSG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.concurrent.thread

import com.sun.jna.platform.win32.WinUser.LowLevelMouseProc

object MouseTracker {

    private val _clickCount = MutableStateFlow(0)
    val clickCount = _clickCount.asStateFlow()

    private var hook: HHOOK? = null
    private var running = false
    private var hookProc: LowLevelMouseProc? = null

    private const val WH_MOUSE_LL = 14
    private const val WM_LBUTTONDOWN = 0x0201
    private const val WM_RBUTTONDOWN = 0x0204

    fun initialize(count: Int) {
        _clickCount.value = count
    }

    fun start() {
        synchronized(this) {
            if (running) return
            running = true
        }

        thread(start = true, isDaemon = true, name = "MouseTrackerThread") {
            installHookAndListen()
        }
    }

    private fun installHookAndListen() {
        hookProc = LowLevelMouseProc { nCode, wParam, _ ->
            if (nCode >= 0) {
                val msg = wParam.toInt()
                if (msg == WM_LBUTTONDOWN || msg == WM_RBUTTONDOWN) {
                    _clickCount.value++
                    SessionManager.incrementMouseClicks()
                }
            }
            User32.INSTANCE.CallNextHookEx(hook, nCode, wParam, null)
        }

        hook = User32.INSTANCE.SetWindowsHookEx(
            WH_MOUSE_LL,
            hookProc,
            Kernel32.INSTANCE.GetModuleHandle(null),
            0,
        )

        val msg = MSG()
        while (running && User32.INSTANCE.GetMessage(msg, null, 0, 0) != 0) {
            User32.INSTANCE.TranslateMessage(msg)
            User32.INSTANCE.DispatchMessage(msg)
        }
    }
}
