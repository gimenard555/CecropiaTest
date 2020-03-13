package com.jimenard.cecropiatest.models

import com.jimenard.cecropiatest.views.adapter.ItemRecyclerView

/**
 * Modelo de datos para los items que se muestran en el recyclerView principal
 * @param itemDeviceImage imagen del dispositivo
 * @param itemDeviceName nombre del dispositivo que se muestra en la lista
 */
data class ItemDevice(
    var itemDeviceImage: String,
    var itemDeviceName: String
) : ItemRecyclerView(itemDeviceImage, itemDeviceName) {

    var itemDeviceIpAddress: String = ""
    var itemDeviceMacAddress: String = ""

    /**
     * SobreCarga del constructor si es necesario
     * @param itemDeviceImage imagen del dispositivo conectado en la red
     * @param itemDeviceMacAddress direccion mac del dispositivo conectado a la red
     * @param itemDeviceIpAddress direccion ip del dispositivo conectado a la red
     * @param itemDeviceName  nombre del dispositivo
     */
    constructor(
        itemDeviceImage: String,
        itemDeviceName: String,
        itemDeviceIpAddress: String,
        itemDeviceMacAddress: String
    )
            : this(
        itemDeviceImage,
        itemDeviceName
    ) {
        this.itemDeviceImage = itemDeviceImage
        this.itemDeviceName = itemDeviceName
        this.itemDeviceIpAddress = itemDeviceIpAddress
        this.itemDeviceMacAddress = itemDeviceMacAddress
    }
}