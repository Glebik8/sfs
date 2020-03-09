package com.blackfish.a1pedal.data

data class Message(
    val content: String,
    val date: String,
    val read: List<Read>,
    val sender: UserInfo,
    val type: String
)