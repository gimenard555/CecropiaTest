package com.jimenard.cecropiatest.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jimenard.cecropiatest.network.MacAddress
import java.net.Inet4Address

@Entity
data class DeviceEntity(
    @PrimaryKey(autoGenerate = true) val deviceId: Long,
    val networkId: Long,
    val ip: Inet4Address,
    val deviceName: String?,
    val hwAddress: MacAddress?
) {


}