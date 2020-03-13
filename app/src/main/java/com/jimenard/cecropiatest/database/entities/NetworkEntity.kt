package com.jimenard.cecropiatest.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jimenard.cecropiatest.network.MacAddress
import com.jimenard.cecropiatest.utils.inet4AddressFromInt
import com.jimenard.cecropiatest.utils.maskWith
import java.net.Inet4Address

@Entity
data class NetworkEntity(
    @PrimaryKey(autoGenerate = true) val networkId: Long,
    val baseIp: Inet4Address,
    val mask: Short,
    val scanId: Long,
    val interfaceName: String,
    val bssid: MacAddress?,
    val ssid: String?
) {
    companion object {
        fun from(
            ip: Inet4Address,
            mask: Short,
            scanId: Long,
            interfaceName: String,
            bssid: MacAddress?,
            ssid: String?
        ): NetworkEntity {
            return NetworkEntity(
                0,
                ip.maskWith(mask),
                mask,
                scanId,
                interfaceName,
                bssid,
                ssid
            )
        }
    }

    fun enumerateAddresses(): Sequence<Inet4Address> {
        return generateSequence(0) {
            val next = it + 1
            if (next < networkSize) next else null
        }
            .map { baseIp.hashCode() + it }
            .map { inet4AddressFromInt("", it) }
    }

    fun containsAddress(host: Inet4Address): Boolean {
        return this.baseIp.maskWith(mask) == host.maskWith(mask)
    }

    val networkSize get() = 2.shl(32 - mask.toInt() - 1)

}