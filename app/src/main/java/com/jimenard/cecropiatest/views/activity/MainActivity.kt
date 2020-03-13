package com.jimenard.cecropiatest.views.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.jimenard.cecropiatest.R
import com.jimenard.cecropiatest.database.entities.DeviceWithNameView
import com.jimenard.cecropiatest.viewmodel.ScanViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DeviceListFragment.OnListFragmentInteractionListener {

    lateinit var viewModel: ScanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.list_devices_fragment)
        val appBarConfiguration = AppBarConfiguration.Builder(navController.graph)
            .setDrawerLayout(null)
            .setFallbackOnNavigateUpListener { false }
            .build()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar
            .setupWithNavController(navController, appBarConfiguration)


        viewModel = ViewModelProviders.of(this).get(ScanViewModel::class.java)

        val interfaceMenu = drawer_navigation.menu.addSubMenu("Interfaces")

        viewModel.fetchAvailableInterfaces().forEach { nic ->
            interfaceMenu.add("${nic.interfaceName} - ${nic.address.hostAddress}/${nic.prefix}").also {
                it.setOnMenuItemClickListener {
                    val bundle = bundleOf("interface_name" to nic.interfaceName)
                    list_devices_fragment.findNavController().navigate(R.id.deviceListFragment, bundle)
                    main_drawer_layout.closeDrawers()
                    true
                }
                it.isCheckable = true
                it.isEnabled = true
            }
        }
        val preferences = drawer_navigation.menu.add("Preferences")
       //preferences.setIcon(R.drawable.ic_settings_white_24dp)
        preferences.setOnMenuItemClickListener {
           // navController.navigate(R.id.appPreferenceFragment)
            main_drawer_layout.closeDrawers()
            true
        }
    }


    override fun onListFragmentInteraction(item: DeviceWithNameView?, view: View) {
        val bundle = bundleOf("deviceId" to item?.deviceId, "deviceIp" to item?.ip)
        list_devices_fragment.findNavController().navigate(R.id.chatList, bundle)
    }

}
