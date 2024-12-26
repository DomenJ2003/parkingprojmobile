package com.example.parkingprojmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingprojmobile.databinding.ActivityMapBinding
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapActivity: AppCompatActivity() {
    lateinit var binding: ActivityMapBinding
    lateinit var mapView: MapView
    var markerList: List<Marker> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mapView = binding.mapView

        val mqttProvider = (application as MyApplication).mqttProvider
        if(!mqttProvider.isConnected) {
            mqttProvider.connect()
        }

        mqttProvider.subscribe { message ->
                println("Received message: $message")
        }

    }

    private fun updateMap() {
        mapView.overlays.clear()
        mapView.overlays.addAll(markerList)
        mapView.invalidate()
    }

    private fun initMap() {
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(15.0)
    }
}