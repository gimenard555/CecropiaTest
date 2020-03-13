package com.jimenard.cecropiatest.network

import com.jimenard.cecropiatest.database.entities.DeviceEntity
import com.jimenard.cecropiatest.database.entities.NetworkEntity
import java.net.Inet4Address
import java.net.NetworkInterface


object LocalMacScanner {
    fun asDevice(network: NetworkEntity): DeviceEntity? {
        val foundMac = getMacAddresses()
            .filterKeys { network.containsAddress(it) }.entries.firstOrNull()
            ?: return null

        return DeviceEntity(0, network.networkId, foundMac.key, null, foundMac.value)
    }

    private fun intToMacAddress(value: ByteArray) =
        MacAddress(value.joinToString(":") { String.format("%02X", it) })

    private fun getMacAddresses(): Map<Inet4Address, MacAddress> {
        return try {
            NetworkInterface.getNetworkInterfaces()
                .toList()
                .flatMap { nic ->
                    nic.interfaceAddresses
                        .mapNotNull {
                            if (it.address is Inet4Address && nic.hardwareAddress != null) {
                                (it.address as Inet4Address) to intToMacAddress(nic.hardwareAddress)
                            } else {
                                null
                            }
                        }
                }
                .associate { it }
        } catch (ex: Exception) { //handle exception
            ex.printStackTrace()
            emptyMap()
        }
    }
}
