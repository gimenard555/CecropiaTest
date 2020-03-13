package com.jimenard.cecropiatest.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jimenard.cecropiatest.utils.Protocol

@Entity
data class PortDescriptionEntity(
    @PrimaryKey
    val portId: Long,
    val port: Int,
    val protocol: Protocol,
    val serviceName: String,
    val serviceDescription: String
) {
    companion object {
        val commonPorts = listOf(
            PortDescriptionEntity(
                0,
                21,
                Protocol.TCP,
                "FTP",
                "File Transfer Protocol"
            ),
            PortDescriptionEntity(
                0,
                22,
                Protocol.TCP,
                "SFTP",
                "Secure FTP"
            ),
            PortDescriptionEntity(
                0,
                80,
                Protocol.TCP,
                "HTTP",
                "Hypertext Transport Protocol"
            ),
            PortDescriptionEntity(
                0,
                53,
                Protocol.UDP,
                "DNS",
                "DNS Server"
            ),
            PortDescriptionEntity(
                0,
                443,
                Protocol.TCP,
                "HTTPS",
                "Secure HTTP"
            ),
            PortDescriptionEntity(
                0,
                548,
                Protocol.TCP,
                "AFP",
                "AFP over TCP"
            ),
            PortDescriptionEntity(
                0,
                8080,
                Protocol.TCP,
                "HTTP-Proxy",
                "HTTP Proxy"
            ),
            PortDescriptionEntity(
                0,
                8000,
                Protocol.TCP,
                "HTTP Alt",
                "HTTP common alternative"
            ),
            PortDescriptionEntity(
                0,
                62078,
                Protocol.TCP,
                "iPhone-Sync",
                "lockdown iOS Service"
            )
        )
    }
}