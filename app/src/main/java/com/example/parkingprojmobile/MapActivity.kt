package com.example.parkingprojmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingprojmobile.databinding.ActivityMapBinding
import com.example.parkingprojmobile.mapUtil.MapHelper
import com.example.parkingprojmobile.mapUtil.MarkerParser
import org.osmdroid.views.MapView

class MapActivity: AppCompatActivity() {
    lateinit var binding: ActivityMapBinding
    lateinit var mapView: MapView
    lateinit var mapHelper: MapHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = binding.mapView

        mapHelper = MapHelper( this, mapView)
        mapHelper.initMapToMyLocation(false)

        val parkingStateList = (application as MyApplication).parkingStateList

        parkingStateList.forEach {
            mapHelper.addMarker(MarkerParser.parseMarkerFromMessage(it, mapView, this))
        }

        binding.backButton.setOnClickListener {
            finish()
        }

    }
}