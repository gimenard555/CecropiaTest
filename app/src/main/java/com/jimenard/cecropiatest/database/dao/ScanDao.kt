package com.jimenard.cecropiatest.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jimenard.cecropiatest.database.entities.ScanEntity

@Dao
interface ScanDao {

    @Insert
    suspend fun insert(scan: ScanEntity): Long

    @Query("Select * FROM ScanEntity")
    fun getAll(): LiveData<List<ScanEntity>>

    @Query("Select * FROM ScanEntity")
    fun getAllNow(): List<ScanEntity>

    @Query("SELECT * FROM ScanEntity WHERE scanId = :scanId")
    fun getById(scanId: Long): LiveData<ScanEntity?>
}
