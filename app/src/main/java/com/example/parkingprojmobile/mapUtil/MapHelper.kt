package com.example.parkingprojmobile.mapUtil

import android.annotation.SuppressLint
import android.location.Location
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapHelper(
    private val activity: AppCompatActivity,
    private val mapView: MapView
) {

    private lateinit var location: Location
    private val markerList: MutableList<Marker> = mutableListOf()

    @SuppressLint("MissingPermission")
    fun initMapToMyLocation(addMarker: Boolean = true) {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

        val requestPermissionLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
                    if (task.isSuccessful && task.result != null) {
                        location = task.result
                        mapView.controller.setZoom(15.0)
                        mapView.controller.setCenter(GeoPoint(task.result.latitude, task.result.longitude))
                        if(addMarker) {
                            val marker = Marker(mapView)
                            marker.position = GeoPoint(task.result.latitude, task.result.longitude)
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            marker.icon = activity.resources.getDrawable(com.example.parkingprojmobile.R.drawable.ic_marker_black, null)
                            marker.title = "Your location"
                            mapView.overlays.add(marker)
                        }
                    }
                }
            }
        }
        requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun getLocation(): Location {
        return location
    }

    fun addMarker(marker: Marker) {
        markerList.add(marker)
        updateMap()

    }

    fun addMarkers(markers: List<Marker>) {
        markerList.addAll(markers)
        updateMap()
    }

    fun clearMarkers() {
        markerList.clear()
        updateMap()
    }

    fun updateMap() {
        mapView.overlays.clear()
        mapView.overlays.addAll(markerList)
        mapView.invalidate()
    }

}
