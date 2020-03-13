package com.jimenard.cecropiatest.views.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jimenard.cecropiatest.R
import com.jimenard.cecropiatest.models.ItemDevice
import kotlinx.android.synthetic.main.item_device_view_layout.view.*

class AItemDevice(layoutItem: Int = R.layout.item_device_view_layout) : AdapterObject(layoutItem) {

    override var foregroundView: Int? = null

    override fun chargeView(
        itemRecyclerView: ItemRecyclerView,
        view: View,
        adapterPosition: Int,
        iNotify: INotify?
    ) {
        val itemDevice = itemRecyclerView as ItemDevice
        Glide.with(view.context)
            .load(itemDevice.itemDeviceImage)
            .apply(RequestOptions.circleCropTransform())
            .into(view.device_image)
        view.device_mac.text = itemDevice.itemDeviceMacAddress
        view.device_name.text = itemDevice.itemDeviceName
        view.container_item_device.setOnClickListener { iNotify!!.itemSelected(adapterPosition) }
    }

    override fun changeViewState(lastView: View, currentView: View) {}
}