package com.jimenard.cecropiatest.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.jimenard.cecropiatest.database.AppDatabase

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.createInstance(application)

    val ipSelected: String = ""
}