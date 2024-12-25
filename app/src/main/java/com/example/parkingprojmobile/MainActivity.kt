package com.example.parkingprojmobile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingprojmobile.api.AuthProvider
import com.example.parkingprojmobile.api.JwtUtil
import com.example.parkingprojmobile.api.MqttProvider
import com.example.parkingprojmobile.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: AuthProvider
    private lateinit var mqttProvider: MqttProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = AuthProvider(this)

        val token = auth.getToken()?: ""

        if(!JwtUtil.isTokenValid(token)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val userName = JwtUtil.getName(token)?: ""
        val userId = JwtUtil.getUserId(token)?: ""


        mqttProvider = MqttProvider(userId, token)

        if(!mqttProvider.isConnected) {
            mqttProvider.connect()
        }
        binding.textView2.text = getString(R.string.hello_user, userName)

        binding.LogOutButton.setOnClickListener {
            auth.clearToken()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.mapButton.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        binding.addParkingButton.setOnClickListener {
            startActivity(Intent(this, AddParkingActivity::class.java))
        }

        binding.liveCameraButton.setOnClickListener {
            startActivity(Intent(this, LiveCameraActivity::class.java))
        }

        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}