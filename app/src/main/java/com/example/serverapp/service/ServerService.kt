package com.example.serverapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.util.Log
import com.example.connectorlibrary.controller.IServerServiceCallback
import com.example.serverapp.model.dao.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@AndroidEntryPoint
class ServerService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}