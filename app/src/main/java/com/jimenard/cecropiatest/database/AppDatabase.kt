package com.jimenard.cecropiatest.database

import android.app.Application
import androidx.room.*
import androidx.room.Database
import com.jimenard.cecropiatest.database.dao.DeviceDao
import com.jimenard.cecropiatest.database.dao.NetworkDao
import com.jimenard.cecropiatest.database.dao.PortDao
import com.jimenard.cecropiatest.database.dao.ScanDao
import com.jimenard.cecropiatest.database.entities.*
import com.jimenard.cecropiatest.network.MacAddress
import com.jimenard.cecropiatest.utils.Protocol
import com.jimenard.cecropiatest.utils.inet4AddressFromInt
import java.net.Inet4Address

@Database(
    entities = [NetworkEntity::class,
        DeviceEntity::class,
        PortEntity::class,
        MacVendorEntity::class,
        ScanEntity::class,
        MessageEntity::class],
    views = [DeviceWithNameView::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun networkDao(): NetworkDao
    abstract fun deviceDao(): DeviceDao
    abstract fun portDao(): PortDao
    abstract fun scanDao(): ScanDao


    companion object {

        var INSTANCE: AppDatabase? = null

        fun createInstance(application: Application): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            application.applicationContext,
                            AppDatabase::class.java,
                            "cecropia-db"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }


    }
}

class Converter {

    @TypeConverter
    fun toInet4Address(value: Int?): Inet4Address? {
        return if (value != null) inet4AddressFromInt("", value) else null
    }

    @TypeConverter
    fun fromInet4Address(value: Inet4Address?): Int? {
        return value?.hashCode()
    }

    @TypeConverter
    fun toProtocol(value: String?): Protocol? {
        if (value == null) return null
        return Protocol.valueOf(value)
    }

    @TypeConverter
    fun fromProtocol(value: Protocol?): String? {
        if (value == null) return null
        return value.name
    }

    @TypeConverter
    fun toMacAddress(value: String?): MacAddress? {
        return if (value != null) MacAddress(value.toUpperCase()) else null
    }

    @TypeConverter
    fun fromMacAddress(value: MacAddress?): String? {
        return value?.address?.toUpperCase()
    }
}