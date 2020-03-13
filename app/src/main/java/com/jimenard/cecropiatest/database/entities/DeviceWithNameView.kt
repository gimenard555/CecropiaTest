package com.jimenard.cecropiatest.database.entities

import androidx.room.DatabaseView
import androidx.room.Ignore
import com.jimenard.cecropiatest.R
import com.jimenard.cecropiatest.database.entities.DeviceEntity
import com.jimenard.cecropiatest.network.MacAddress
import java.net.Inet4Address

@DatabaseView("SELECT DeviceEntity.deviceId, DeviceEntity.networkId, DeviceEntity.ip, DeviceEntity.hwAddress, DeviceEntity.deviceName, MacVendorEntity.name as vendorName FROM DeviceEntity LEFT JOIN MacVendorEntity ON MacVendorEntity.mac = substr(DeviceEntity.hwAddress, 0, 9)")
data class DeviceWithNameView(
    val deviceId: Long,
    val networkId: Long,
    val ip: Inet4Address,
    val hwAddress: MacAddress?,
    val deviceName: String?,
    val vendorName: String?
) {
    @Ignore
    val asDevice = DeviceEntity(
        deviceId,
        networkId,
        ip,
        deviceName,
        hwAddress
    )

    val icon
        get() = when (vendorName) {
            "Espressif Inc." -> R.drawable.ic_memory_white_48dp
            else -> R.drawable.ic_laptop_white_48dp
        }
}