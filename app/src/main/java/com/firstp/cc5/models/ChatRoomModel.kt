package com.firstp.cc5.models



data class ChatRoomModel(
    val chatRoomId : String ="",
    val userIds : ArrayList<String> = ArrayList(),
    val lastMessageTimestamp : Long = System.currentTimeMillis(),

    val lastMessageSenderId : String = ""
)
