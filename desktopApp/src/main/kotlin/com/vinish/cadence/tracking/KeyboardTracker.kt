package com.vinish.cadence.tracking
import com.sun.jna.platform.win32.*
import com.sun.jna.platform.win32.WinUser.MSG
import com.sun.jna.platform.win32.WinUser.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object KeyboardTracker {

    private val _typingCount = MutableStateFlow(0)
    val typingCount = _typingCount.asStateFlow()

    private var hook: HHOOK? = null

    fun start() {

        val hookProc = LowLevelKeyboardProc { nCode, wParam, _ ->

            if (nCode >= 0) {

                if (wParam.toInt() == WM_KEYDOWN) {

                    _typingCount.value++
                }
            }

            User32.INSTANCE.CallNextHookEx(
                hook,
                nCode,
                wParam,
                null
            )
        }

        hook = User32.INSTANCE.SetWindowsHookEx(
            WH_KEYBOARD_LL,
            hookProc,
            Kernel32.INSTANCE.GetModuleHandle(null),
            0
        )

        val msg = MSG()

        while (User32.INSTANCE.GetMessage(msg, null, 0, 0) != 0) {

            User32.INSTANCE.TranslateMessage(msg)
            User32.INSTANCE.DispatchMessage(msg)
        }
    }
}