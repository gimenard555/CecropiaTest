package com.jimenard.cecropiatest.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["name", "mac"])
data class MacVendorEntity(val name: String, val mac: String)