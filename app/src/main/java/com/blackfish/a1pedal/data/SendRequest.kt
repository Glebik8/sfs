package com.blackfish.a1pedal.data

import com.google.gson.annotations.SerializedName


data class SendRequest(
        @SerializedName("chat_id")
        val chatId: Int,
        val message: Message,
        val recipient: UserInfo,
        val sender: UserInfo
)