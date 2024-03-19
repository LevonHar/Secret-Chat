package com.example.secretchat.screens

import androidx.lifecycle.ViewModel
import com.example.secretchat.models.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChatViewModel:ViewModel() {
    private val _messages:MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val messages = _messages.asStateFlow()

    private val client:OkHttpClient = OkHttpClient()
    var webSocket:WebSocket? = null

    fun connect(){
        webSocket = client.newWebSocket(createRequest(),object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val message = Json.decodeFromString<Message>(text)
                println("new message received: $message")
                _messages.update { it+message }
            }
        })
    }
    fun sendMessage(message: Message){
        webSocket?.send(Json.encodeToString(Message.serializer(),message))
    }
    fun shutdown(){
        webSocket?.close(100,"Closed Manually")
        client.dispatcher.executorService.shutdown()
    }
    fun createRequest():Request{
        return Request
            .Builder()
            .url("ws://192.168.10.49:8080/chat")
            .build()
    }
}