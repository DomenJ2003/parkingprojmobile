package com.example.parkingprojmobile

import android.app.Application
import com.example.parkingprojmobile.api.AuthProvider
import com.example.parkingprojmobile.api.JwtUtil
import com.example.parkingprojmobile.api.MqttProvider

class MyApplication: Application() {

    lateinit var authProvider: AuthProvider
    lateinit var mqttProvider: MqttProvider

    override fun onCreate() {
        super.onCreate()
        authProvider = AuthProvider(this)
        val token = authProvider.getToken()?: ""
        val userName = JwtUtil.getName(token)?: ""
        val userId = JwtUtil.getUserId(token)?: ""


        mqttProvider = MqttProvider(userId, token)

        if(!mqttProvider.isConnected) {
            mqttProvider.connect()
        }


    }
}