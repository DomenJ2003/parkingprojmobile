//package com.example.parkingprojmobile.mapUtil
//
//import com.example.parkingprojmobile.ParkingState
//import com.google.gson.Gson
//import org.osmdroid.views.MapView
//import org.osmdroid.views.overlay.Marker
//
//object MarkerParser {
//    fun parseMarkerFromMessage(marker: String, mapView: MapView): Marker {
//        val gson = Gson()
//        var parkingState = gson.fromJson<ParkingState>(marker, String::class.java)
//        val newMarker = Marker(mapView)
//    }
//}