package com.firstp.cc5.models

import com.google.firebase.Timestamp

data class MessageModel(

    val message : String = "",
    val senderId : String = "",
    val timestamp : Long = System.currentTimeMillis()
)
