package com.jimenard.cecropiatest.views.activity

import android.os.Bundle
import android.provider.SyncStateContract
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import com.jimenard.cecropiatest.R
import com.jimenard.cecropiatest.database.entities.DeviceEntity
import com.jimenard.cecropiatest.database.entities.PortEntity
import com.jimenard.cecropiatest.network.PortScanner
import com.jimenard.cecropiatest.utils.AppPreferences
import com.jimenard.cecropiatest.viewmodel.ScanViewModel
import com.jimenard.cecropiatest.views.recycler.UtilRecyclerView
import kotlinx.android.synthetic.main.chat_list_fragment_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess

class ChatList : Fragment() {

    lateinit var viewModel: ScanViewModel
    private var datagramSocket: DatagramSocket? = null
    private var inetAddress: InetAddress? = null
    private var threadMessages: Thread? = null
    private var threadSendMessage: Thread? = null
    var datagramPacket: DatagramPacket? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chat_list_fragment_layout, container, false)
        viewModel = ViewModelProviders.of(activity!!).get(ScanViewModel::class.java)
    //    val recyclerView = view.findViewById<UtilRecyclerView>(R.id.list)
        val argumentDeviceId = arguments?.getLong("deviceId")!!

        viewModel.deviceDao.getById(argumentDeviceId).observe(viewLifecycleOwner, Observer {
            fetchInfo(it.asDevice)
            view.findViewById<TextView>(R.id.deviceIpTextView).text = it.ip.hostAddress
            view.findViewById<TextView>(R.id.deviceHwAddressTextView).text =
                it.hwAddress?.getAddress(AppPreferences(this).hideMacDetails)
            createObjects(it.ip.hostAddress)
            initChatViews()
        })


        /*recyclerView.setHandler(context!!, this, object :
            RecyclerViewCommon.Handler<Port>(R.layout.fragment_port_item, ports) {
            override fun shareIdentity(a: Port, b: Port) = a.port == b.port
            override fun areContentsTheSame(a: Port, b: Port) = a == b
            override fun onClickListener(view: View, value: Port) {
                viewModel.viewModelScope.launch(context = Dispatchers.IO) {
                    val ip = viewModel.deviceDao.getByIdNow(value.deviceId).ip
                    withContext(Dispatchers.Main) {
                        Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("http://${ip}:${value.port}")
                        }.also {
                            startActivity(it)
                        }
                    }
                }
            }

            override fun bindItem(view: View): (value: Port) -> Unit {
                val portNumberTextView: TextView = view.portNumberTextView
                val protocolTextView: TextView = view.protocolTextView
                val serviceTextView: TextView = view.serviceNameTextView

                return { item ->
                    portNumberTextView.text = item.port.toString()
                    protocolTextView.text = item.protocol.toString()
                    serviceTextView.text = item.description?.serviceName

                }
            }

        })*/


        return view
    }

    @Throws(UnknownHostException::class, SocketException::class)
    private fun createObjects(ipString: String) {
        this.inetAddress = InetAddress.getByName(ipString)
        this.datagramSocket = DatagramSocket()
    }

    /**
     *
     */
    private fun initChatViews() {
        //Thread para pegar as mensagens chegar....
        this.threadMessages = Thread(Runnable {
            while (true) {
                try {
                    val response = DatagramPacket(ByteArray(512), 512)
                    this.datagramSocket!!.receive(response)
                    val printResposta = String(response.data)
                    if (response.socketAddress == datagramPacket!!.socketAddress) {
                        if (printResposta.trim { it <= ' ' }.startsWith("Desconectado...")) {
                            exitProcess(0)
                        }
                    }
                    var text: String = this.messageHistory.text.toString()
                    text = text + printResposta + "\n"
                    val finalTexto = text
                    activity!!.runOnUiThread(Runnable { this.messageHistory.text = finalTexto })
                } catch (ex: IOException) {
                    Logger.getLogger(MainActivity::class.java.name)
                        .log(Level.SEVERE, null, ex)
                }
            }
        })

        this.threadMessages!!.start()

        this.threadSendMessage = Thread(Runnable {
            send_button_chat_log.setOnClickListener {
                if (chat_message_text.text.toString() == "") {
                    println("Se desconecta el cliente")
                }
                try {
                    val buffer = chat_message_text.text.toString().toByteArray()


                    datagramPacket = DatagramPacket(buffer, buffer.size, this.inetAddress, 4545)
                    this.datagramSocket!!.send(datagramPacket)
                    chat_message_text.setText("")
                } catch (ex: IOException) {
                    println("IOExeption: " + ex.message)
                }
            }

        })
        this.threadSendMessage!!.start()
    }

    private fun fetchInfo(device: DeviceEntity) {
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                PortScanner(device.ip).scanPorts().forEach {
                    launch {
                        val result = it.await()
                        if (result.isOpen) {
                            viewModel.portDao.upsert(
                                PortEntity(
                                    0,
                                    result.port,
                                    result.protocol,
                                    device.deviceId
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}