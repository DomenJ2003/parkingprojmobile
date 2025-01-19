package com.example.parkingprojmobile.mapUtil

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon


class MapHelper(
    private val activity: AppCompatActivity,
    private val mapView: MapView
) {

    private lateinit var location: Location
    private val markerList: MutableList<Marker> = mutableListOf()
    private val polygonList: MutableList<Polygon> = mutableListOf()

    fun setMapPosition(lat: Double, lon: Double, zoomLevel: Double = 15.0){
        mapView.controller.setZoom(zoomLevel)
        mapView.controller.setCenter(GeoPoint(lat, lon))
    }

    fun initMapToMyLocation(addMarker: Boolean = true) {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

        val requestPermissionLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        1
                    )
                    return@registerForActivityResult
                }
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
                            markerList.add(marker)
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

    fun addPolygons(polygons: List<Polygon>){
        polygonList.addAll(polygons)
        updateMap()
    }

    fun updateMap() {
        mapView.overlays.clear()
        mapView.overlays.addAll(markerList)
        mapView.overlays.addAll(polygonList)

        mapView.invalidate()
    }

}
