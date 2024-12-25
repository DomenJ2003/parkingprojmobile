package com.example.parkingprojmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingprojmobile.databinding.ActivityLiveCameraBinding

class LiveCameraActivity: AppCompatActivity() {
    lateinit var binding: ActivityLiveCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}