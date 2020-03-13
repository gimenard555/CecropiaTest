package com.jimenard.cecropiatest.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jimenard.cecropiatest.database.entities.DeviceEntity
import com.jimenard.cecropiatest.database.entities.DeviceWithNameView
import com.jimenard.cecropiatest.network.MacAddress
import java.net.Inet4Address

@Dao
interface DeviceDao {
    @Query("Select * FROM DeviceWithNameView WHERE networkId = :networkId ORDER BY ip ASC")
    fun getAll(networkId: Long): LiveData<List<DeviceWithNameView>>


    @Query("SELECT * FROM DeviceEntity WHERE networkId = :networkId")
    fun getAllNow(networkId: Long): List<DeviceEntity>

    @Insert
    fun insert(device: DeviceEntity): Long

    @Transaction
    fun upsert(device: DeviceEntity): Long {
        val existingDevice = getByAddressInNetwork(device.ip, device.networkId)
        return if (existingDevice == null) {
            insert(device)
        } else {
            update(device.copy(deviceId = existingDevice.deviceId))
            existingDevice.deviceId
        }
    }

    @Transaction
    fun upsertName(networkId: Long, ip: Inet4Address, name: String, allowNew: Boolean = true) {
        val existingDevice = getByAddressInNetwork(ip, networkId)
        if (existingDevice != null) {
            updateServiceName(existingDevice.deviceId, name)
        } else if (allowNew) {
            insert(DeviceEntity(0, networkId, ip, name, null))
        }
    }

    @Transaction
    fun upsertHwAddress(
        networkId: Long,
        ip: Inet4Address,
        hwAddress: MacAddress,
        allowNew: Boolean
    ) {
        val existingDevice = getByAddressInNetwork(ip, networkId)
        if (existingDevice != null) {
            updateHwAddress(existingDevice.deviceId, hwAddress)
        } else if (allowNew) {
            insert(DeviceEntity(0, networkId, ip, null, hwAddress))
        }
    }

    @Update
    fun update(device: DeviceEntity)

    @Query("SELECT * FROM DeviceWithNameView WHERE deviceId = :id")
    fun getById(id: Long): LiveData<DeviceWithNameView>

    @Query("SELECT * FROM DeviceEntity WHERE deviceId = :id")
    fun getByIdNow(id: Long): DeviceEntity

    @Query("SELECT * FROM DeviceEntity WHERE ip = :ip AND networkId = :networkId")
    fun getByAddressInNetwork(ip: Inet4Address, networkId: Long): DeviceEntity?

    @Query("SELECT * FROM DeviceEntity WHERE ip = :ip AND networkId IN (SELECT networkId FROM NetworkEntity WHERE scanId = :scanId)")
    fun getByAddress(ip: Inet4Address, scanId: Long): DeviceEntity?

    @Query("UPDATE DeviceEntity SET hwAddress = :hwAddress WHERE deviceId = :deviceId")
    fun updateHwAddress(deviceId: Long, hwAddress: MacAddress)

    @Query("UPDATE DeviceEntity SET deviceName = :deviceName WHERE deviceId = :deviceId")
    fun updateServiceName(deviceId: Long, deviceName: String?)

    @Transaction
    fun insertIfNew(networkId: Long, ip: Inet4Address): Long {
        val existingAddress = getByAddressInNetwork(ip, networkId)
            ?: return insert(DeviceEntity(0, networkId, ip, null, null))
        return existingAddress.deviceId
    }
}