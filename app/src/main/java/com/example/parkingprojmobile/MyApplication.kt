package com.example.parkingprojmobile

import android.app.Application
import com.example.parkingprojmobile.api.ApiUtil
import com.example.parkingprojmobile.api.AuthProvider
import com.example.parkingprojmobile.api.JwtUtil
import com.example.parkingprojmobile.api.MqttProvider
import com.example.parkingprojmobile.data.Parking
import com.example.parkingprojmobile.data.ParkingState
import com.example.parkingprojmobile.mapUtil.MarkerParser

class MyApplication: Application() {

    lateinit var authProvider: AuthProvider
    lateinit var mqttProvider: MqttProvider
    val parkingStateList: MutableList<ParkingState> = mutableListOf()

    override fun onCreate() {
        super.onCreate()
        authProvider = AuthProvider(this)
        val token = authProvider.getToken()?: ""
        var userId = ""
        try {
            userId = JwtUtil.getUserId(token)?: ""
        } catch (e: Exception) {
            authProvider.clearToken()
        }

        mqttProvider = MqttProvider(userId, token)

        if(!mqttProvider.isConnected) {
            mqttProvider.connect()
        }

        mqttProvider.subscribe { message ->
            val parkingState = MarkerParser.parseMarkerState(message)
            if(parkingState != null){
                parkingStateList.add(parkingState)
            }
        }
    }
}