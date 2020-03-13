package com.jimenard.cecropiatest.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.jimenard.cecropiatest.database.AppDatabase
import com.jimenard.cecropiatest.database.entities.NetworkEntity
import com.jimenard.cecropiatest.repository.ScanRepository

class ScanViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.createInstance(application)

    val deviceDao = db.deviceDao()
    private val networkDao = db.networkDao()
    val portDao = db.portDao()
    private val scanDao = db.scanDao()
    private val networkScanRepository = ScanRepository(networkDao, scanDao, deviceDao, application)
    val scanProgress by lazy { MutableLiveData<ScanRepository.ScanProgress>() }
    private val currentNetworkId = MutableLiveData<Long>()
    val currentScanId = MutableLiveData<Long>()

    val devices = Transformations.switchMap(currentNetworkId) {
        deviceDao.getAll(it)
    }

    val currentNetworks = Transformations.switchMap(currentScanId) {
        Log.d("asd", "netowkrsadss $it")
        networkDao.getAll(it)
    }

    fun fetchAvailableInterfaces() = networkScanRepository.fetchAvailableInterfaces()

    suspend fun startScan(interfaceName: String): NetworkEntity {
        val network = networkScanRepository.startScan(interfaceName, scanProgress, currentNetworkId)
        currentScanId.value = network.scanId
        return network
    }
}