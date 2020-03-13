package com.jimenard.cecropiatest.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScanEntity(@PrimaryKey(autoGenerate = true) val scanId: Long, val startedAt: Long)