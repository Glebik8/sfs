package com.blackfish.a1pedal.data

data class DialogInfo(
    val date: String,
    val last_message: String,
    val last_message_type: String,
    val pk: Int,
    val read: List<Read>,
    val users: List<UserInfo>
)