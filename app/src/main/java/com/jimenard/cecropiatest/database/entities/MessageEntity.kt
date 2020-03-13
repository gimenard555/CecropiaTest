package com.jimenard.cecropiatest.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val messageId: Long,
    val transmitterIp: String,
    val receiverIp: String,
    val deviceIp: String,
    val message: String,
    val messageNum:Int
)