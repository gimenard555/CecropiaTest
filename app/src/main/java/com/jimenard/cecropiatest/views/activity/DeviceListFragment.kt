package com.jimenard.cecropiatest.views.activity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jimenard.cecropiatest.R
import com.jimenard.cecropiatest.database.entities.DeviceWithNameView
import com.jimenard.cecropiatest.repository.ScanRepository
import com.jimenard.cecropiatest.utils.AppPreferences
import com.jimenard.cecropiatest.viewmodel.ChatViewModel
import com.jimenard.cecropiatest.viewmodel.ScanViewModel
import com.jimenard.cecropiatest.views.recycler.UtilRecyclerView
import kotlinx.android.synthetic.main.item_device_view_layout.view.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass.
 * Use the [DeviceListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeviceListFragment : Fragment() {
    private var listener: OnListFragmentInteractionListener? = null
    private val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(ScanViewModel::class.java)
    }

    private val chatViewModel by lazy {
        ViewModelProviders.of(activity!!).get(ChatViewModel::class.java)
    }
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var emptyListInfo: View

    private lateinit var argumentInterfaceName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_device_list_layout, container, false)
        emptyListInfo = view.findViewById<View>(R.id.swipeDownViewImage)
        swipeRefreshLayout = view.findViewById(R.id.swipeDownView)
        argumentInterfaceName = arguments?.getString("interface_name")!!


        viewModel.devices.observe(viewLifecycleOwner, Observer {
            emptyListInfo.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        })

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        viewModel.scanProgress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ScanRepository.ScanProgress.ScanFinished -> {
                    progressBar.visibility = View.INVISIBLE
                    swipeRefreshLayout.isRefreshing = false
                }
                is ScanRepository.ScanProgress.ScanRunning -> {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = (it.progress * 1000.0).roundToInt()
                }
                is ScanRepository.ScanProgress.ScanNotStarted -> progressBar.visibility =
                    View.INVISIBLE
            }
        })

        val devicesList = view.findViewById<UtilRecyclerView>(R.id.devicesList)
        devicesList.setHandler(
            context!!,
            this,
            object : UtilRecyclerView.Handler<DeviceWithNameView>(
                R.layout.item_device_view_layout,
                viewModel.devices
            ) {
                override fun bindItem(view: View): (DeviceWithNameView) -> Unit {
                    val device_mac: TextView = view.device_mac
                    val device_name: TextView = view.device_name
                    val device_image: ImageView = view.device_image
                    return { item ->
                        device_mac.text =
                            item.hwAddress?.getAddress(AppPreferences(this@DeviceListFragment).hideMacDetails)
                        device_name.text = item.ip.hostAddress
                        Glide.with(view.context)
                            .load("")
                            .placeholder(R.drawable.ic_computer_pc_svgrepo_com)
                            .apply(RequestOptions.circleCropTransform())
                            .into(device_image)
                    }
                }

                override fun onClickListener(view: View, value: DeviceWithNameView) {
                    listener?.onListFragmentInteraction(value, view)
                }

                override fun shareIdentity(a: DeviceWithNameView, b: DeviceWithNameView) =
                    a.deviceId == b.deviceId

                override fun areContentsTheSame(a: DeviceWithNameView, b: DeviceWithNameView) =
                    a == b

            })

        swipeRefreshLayout.setOnRefreshListener {
            runScan()
        }

        return view
    }


    private fun runScan() {
        viewModel.viewModelScope.launch {
            viewModel.startScan(argumentInterfaceName)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: DeviceWithNameView?, view: View)
    }
}
