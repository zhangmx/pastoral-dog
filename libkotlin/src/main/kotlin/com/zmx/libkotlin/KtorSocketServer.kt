package com.zmx.libkotlin

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*

// https://ktor.io/docs/servers-raw-sockets.html
fun main(args: Array<String>) {
    runBlocking {
        val selectorManager = SelectorManager(Dispatchers.IO)
        val serverSocket = aSocket(selectorManager).tcp().bind("127.0.0.1", 9002)
        println("Server is listening at ${serverSocket.localAddress}")
        while (true) {
            val socket = serverSocket.accept()
            println("Accepted $socket")
            val receiveChannel = socket.openReadChannel()
            val sendChannel = socket.openWriteChannel(autoFlush = true)
            launch {
                sendChannel.writeStringUtf8("Please enter your name\n")
                try {
                    while (true) {
                        val name = receiveChannel.readUTF8Line()
                        sendChannel.writeStringUtf8("Hello, $name!\n")
                    }
                } catch (e: Throwable) {
                    socket.close()
                }
            }
            // server push data to client
            launch {
                try {
                    while (true) {
                        delay(1000)
                        val randomInt = (Math.random() * 100).toInt()
                        sendChannel.writeStringUtf8("Hello, $randomInt!\n")
                    }
                } catch (e: Throwable) {
                    socket.close()
                }
            }
        }
    }
}