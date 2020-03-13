package com.jimenard.cecropiatest.network

import com.jimenard.cecropiatest.database.entities.NetworkEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.net.Inet4Address

class PingScanner(
    val network: NetworkEntity,
    val onUpdate: (ScanResult) -> Unit
) {

    suspend fun pingIpAddresses() :  List<ScanResult> =
        withContext(Dispatchers.IO) {
            network.enumerateAddresses().chunked(10).map { ipAddresses ->
                async {
                    ipAddresses.map { ipAddress ->
                        val isReachable = ipAddress.isReachable(5000)
                        val result = ScanResult(ipAddress, isReachable, 1.0/ network.networkSize)
                        onUpdate(result)
                        result
                    }
                }
            }.toList().awaitAll().flatten()
        }

    data class ScanResult(val ipAddress: Inet4Address, val isReachable: Boolean, val progressIncrease: Double)
}

