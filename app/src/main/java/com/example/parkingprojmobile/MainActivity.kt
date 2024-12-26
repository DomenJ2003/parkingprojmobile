package com.example.parkingprojmobile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingprojmobile.api.AuthProvider
import com.example.parkingprojmobile.api.JwtUtil
import com.example.parkingprojmobile.api.MqttProvider
import com.example.parkingprojmobile.databinding.ActivityMainBinding
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val context = applicationContext
        Configuration.getInstance().load(context, getPreferences(MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = BuildConfig.LIBRARY_PACKAGE_NAME

        val auth = (application as MyApplication).authProvider

        val token = auth.getToken()?: ""


        val userName = JwtUtil.getName(token)?: ""

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

        binding.myParkingsButton.setOnClickListener {
            startActivity(Intent(this, ParkingListActivity::class.java))
        }
    }
}