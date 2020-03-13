package com.jimenard.cecropiatest.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jimenard.cecropiatest.database.entities.NetworkEntity

@Dao
interface NetworkDao {

    @Query("Select * FROM NetworkEntity WHERE scanId = :scanId")
    fun getAll(scanId: Long): LiveData<List<NetworkEntity>>


    @Query("Select * FROM NetworkEntity WHERE scanId = :scanId")
    fun getAllNow(scanId: Long): List<NetworkEntity>

    @Insert
    fun insert(network: NetworkEntity): Long

    @Insert
    fun insertAll(vararg networks: NetworkEntity)

    @Query("SELECT * FROM NetworkEntity WHERE networkId = :networkId")
    fun getByIdNow(networkId: Long): NetworkEntity


    @Query("SELECT * FROM NetworkEntity WHERE networkId = :networkId")
    fun getById(networkId: Long): LiveData<NetworkEntity>
}
