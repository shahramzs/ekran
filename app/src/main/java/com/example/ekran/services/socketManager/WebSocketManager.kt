package com.example.ekran.services.socketManager

import com.example.ekran.services.http.Api
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import timber.log.Timber
import java.net.URI


class WebSocketManager:SocketManagerService, WebSocketClient(URI(Api.SOCKET_URL)) {

//        private lateinit var webSocketClient: WebSocketClient

//    private var messageData:String = ""
//
//
//    override fun webSocketConnect(webSocketUrl: String): String {
//
//        val uri: URI = URI(webSocketUrl)
//        webSocketClient = object : WebSocketClient(uri) {
//            override fun onOpen(handshakedata: ServerHandshake?) {
//                Timber.tag("onOpen").d(handshakedata.toString())
//            }
//
//            override fun onMessage(message: String?) {
//                Timber.tag("message").d(message)
//                messageData = message!!
//            }
//
//            override fun onClose(code: Int, reason: String?, remote: Boolean) {
//                Timber.tag("onClose").d(reason.toString())
//            }
//
//            override fun onError(ex: Exception?) {
//                Timber.tag("onError").e(ex)
//            }
//        }
//        webSocketClient.connect()
//
//        return messageData
//    }
//
//    override fun webSocketClose() {
//        webSocketClient.close()
//    }


    override fun webSocketConnect(webSocketUrl: String): String {
        TODO("Not yet implemented")
    }

    override fun webSocketClose() {
        TODO("Not yet implemented")
    }

    override fun onOpen(handshakedata: ServerHandshake?) {
        Timber.tag("onOpen").d(handshakedata.toString())
    }

    override fun onMessage(message: String?) {
        Timber.tag("message").d(message)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        Timber.tag("onClose").d(reason.toString())
    }

    override fun onError(ex: Exception?) {
        Timber.tag("socketOnError").e(ex)
    }

}