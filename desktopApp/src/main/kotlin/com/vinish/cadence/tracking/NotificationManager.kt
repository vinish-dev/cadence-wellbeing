package com.vinish.cadence.tracking

import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.image.BufferedImage
import java.awt.Image

object NotificationManager {
    fun sendNotification(title: String, message: String) {
        if (!SystemTray.isSupported()) return
        
        val tray = SystemTray.getSystemTray()
        val image: Image = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
        val trayIcon = TrayIcon(image, "Cadence")
        trayIcon.isImageAutoSize = true
        
        try {
            tray.add(trayIcon)
            trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO)
            
            Thread {
                Thread.sleep(5000)
                tray.remove(trayIcon)
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
