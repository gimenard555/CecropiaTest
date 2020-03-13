package com.jimenard.cecropiatest.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jimenard.cecropiatest.utils.Protocol

@Entity
data class PortEntity(
    @PrimaryKey(autoGenerate = true) val portId: Long, val port: Int,
    val protocol: Protocol,
    val deviceId: Long
) {
    val description get() = PortDescriptionEntity.commonPorts.find { it.port == port }
}