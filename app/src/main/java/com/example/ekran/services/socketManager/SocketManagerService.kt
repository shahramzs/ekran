package com.example.ekran.services.socketManager

interface SocketManagerService {

    fun webSocketConnect(webSocketUrl:String): String

    fun webSocketClose()
}