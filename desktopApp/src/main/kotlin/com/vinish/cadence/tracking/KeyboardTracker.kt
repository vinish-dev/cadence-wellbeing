package com.vinish.cadence.tracking

import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinUser.HHOOK
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc
import com.sun.jna.platform.win32.WinUser.MSG
import com.sun.jna.platform.win32.WinUser.WH_KEYBOARD_LL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.concurrent.thread

object KeyboardTracker {

    private const val WM_KEYDOWN = 0x0100
    private const val WM_KEYUP = 0x0101
    private const val WM_SYSKEYDOWN = 0x0104
    private const val WM_SYSKEYUP = 0x0105

    private val pressedKeys = BooleanArray(256)

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
        hookProc = LowLevelKeyboardProc { nCode, wParam, info ->
            if (nCode >= 0 && info != null) {
                val msg = wParam.toInt()
                val vkCode = info.vkCode
                
                if (vkCode in 0..255) {
                    if (msg == WM_KEYDOWN || msg == WM_SYSKEYDOWN) {
                        if (!pressedKeys[vkCode]) {
                            pressedKeys[vkCode] = true
                            _typingCount.value++
                            SessionManager.incrementKeysTyped()
                        }
                    } else if (msg == WM_KEYUP || msg == WM_SYSKEYUP) {
                        pressedKeys[vkCode] = false
                    }
                }
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
