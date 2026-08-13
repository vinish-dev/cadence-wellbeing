package com.vinish.cadence.tracking

import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinUser.HHOOK
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc
import com.sun.jna.platform.win32.WinUser.MSG
import com.sun.jna.platform.win32.WinUser.WH_KEYBOARD_LL
import com.sun.jna.platform.win32.WinUser.WM_KEYDOWN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.concurrent.thread

object KeyboardTracker {

    private val _typingCount = MutableStateFlow(0)
    val typingCount = _typingCount.asStateFlow()

    private var hook: HHOOK? = null
    private var running = false
    private var hookProc: LowLevelKeyboardProc? = null

    fun initialize(count: Int) {
        _typingCount.value = count
    }

    fun start() {
        synchronized(this) {
            if (running) return
            running = true
        }

        thread(start = true, isDaemon = true, name = "KeyboardTrackerThread") {
            installHookAndListen()
        }
    }

    private fun installHookAndListen() {
        hookProc = LowLevelKeyboardProc { nCode, wParam, _ ->
            if (nCode >= 0 && wParam.toInt() == WM_KEYDOWN) {
                _typingCount.value++
                SessionManager.incrementKeysTyped()
            }

            User32.INSTANCE.CallNextHookEx(hook, nCode, wParam, null)
        }

        hook = User32.INSTANCE.SetWindowsHookEx(
            WH_KEYBOARD_LL,
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
