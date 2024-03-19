package com.example.secretchat.models

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val name: String,
    val text: String
)