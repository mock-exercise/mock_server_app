package com.example.serverapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ServerService : Service() {

    override fun onBind(intent: Intent): IBinder? {
       return null
    }
}