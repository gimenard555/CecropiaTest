package com.jimenard.cecropiatest.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jimenard.cecropiatest.database.entities.PortEntity

@Dao
interface  PortDao {

    @Insert
    fun insert(port: PortEntity): Long

    @Transaction
    suspend fun upsert(port: PortEntity): Long {
        val portFromDB = getPortFromNumber(port.deviceId, port.port) ?: return insert(port)

        update(PortEntity(portFromDB.portId, port.port, port.protocol, port.deviceId))
        return portFromDB.portId
    }

    @Update
    fun update(port: PortEntity)

    @Query("SELECT * FROM PortEntity WHERE deviceId = :deviceId AND port = :port")
    fun getPortFromNumber(deviceId: Long, port: Int): PortEntity?


    @Query("SELECT * FROM PortEntity WHERE deviceId = :deviceId")
    fun getAllForDevice(deviceId: Long): LiveData<List<PortEntity>>
}
