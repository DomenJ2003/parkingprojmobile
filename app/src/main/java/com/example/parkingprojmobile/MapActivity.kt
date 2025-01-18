package com.example.parkingprojmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingprojmobile.api.ApiUtil
import com.example.parkingprojmobile.data.Parking
import com.example.parkingprojmobile.databinding.ActivityMapBinding
import com.example.parkingprojmobile.mapUtil.MapHelper
import com.example.parkingprojmobile.mapUtil.MarkerParser
import com.google.android.gms.common.api.Api
import org.osmdroid.views.MapView

class MapActivity: AppCompatActivity() {
    lateinit var binding: ActivityMapBinding
    lateinit var mapView: MapView
    lateinit var mapHelper: MapHelper
    lateinit var apiUtil: ApiUtil
    lateinit var allParkings: List<Parking>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = binding.mapView

        mapHelper = MapHelper( this, mapView)
        mapHelper.initMapToMyLocation(false)

        apiUtil = ApiUtil(this)

        apiUtil.getAllParkings { parkings ->
            allParkings = parkings.toList()
            initAllParkings()
        }

        val parkingStateList = (application as MyApplication).parkingStateList

        parkingStateList.forEach {
            mapHelper.addMarker(MarkerParser.parseMarkerFromMessage(it, mapView, this))
        }

        binding.backButton.setOnClickListener {
            finish()
        }

    }

    private fun initAllParkings(){
        allParkings.forEach {
            println(it.geometry.coordinates.size)
        }
    }
}