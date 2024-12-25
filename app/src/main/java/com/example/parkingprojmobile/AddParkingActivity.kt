package com.example.parkingprojmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingprojmobile.databinding.ActivityAddParkingBinding

class AddParkingActivity: AppCompatActivity() {
    lateinit var binding: ActivityAddParkingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}