package com.vinish.cadence

import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import kotlin.system.exitProcess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SingleInstanceManager {
    private const val PORT = 38412
    private val _showWindowRequests = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val showWindowRequests = _showWindowRequests.asSharedFlow()

    fun acquireOrExit() {
        try {
            val socket = Socket("127.0.0.1", PORT)
            // If we connect, another instance is already listening!
            socket.getOutputStream().write("SHOW\n".toByteArray())
            socket.close()
            // Exit immediately since we are the secondary instance
            exitProcess(0)
        } catch (e: Exception) {
            // Connection refused means no other instance is listening.
            // We can start our own server.
            startServer()
        }
    }

    private fun startServer() {
        Thread {
            try {
                val serverSocket = ServerSocket(PORT, 1, InetAddress.getByName("127.0.0.1"))
                while (true) {
                    val client = serverSocket.accept()
                    try {
                        val reader = client.getInputStream().bufferedReader()
                        val msg = reader.readLine()
                        if (msg == "SHOW") {
                            _showWindowRequests.tryEmit(Unit)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        client.close()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.apply {
            isDaemon = true
            name = "SingleInstanceServerThread"
            start()
        }
    }
}
