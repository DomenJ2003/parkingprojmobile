package com.example.parkingprojmobile

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingprojmobile.databinding.ActivityAddParkingBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker



class AddParkingActivity: AppCompatActivity() {
    lateinit var binding: ActivityAddParkingBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var location: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestLocationPermission()
        val gson = Gson()
        var hourValue: Int = 0
        var minuteValue: Int = 0

        binding.timePickerButton.setOnClickListener() {
            showTimePicker { hour, minute ->
                hourValue = hour
                minuteValue = minute
            }
        }

        binding.saveButton.setOnClickListener() {
            val openFreeParking = binding.openFreeParking.isChecked
            val openPaidParking = binding.openPaidParking.isChecked

            val mqttProvider = (application as MyApplication).mqttProvider

            val parking = ParkingState(
                location.latitude,
                location.longitude,
                openFreeParking,
                openPaidParking,
                hourValue,
                minuteValue,
            )

            val messageJson = gson.toJson(parking)
            mqttProvider.publish(messageJson.toString())
            finish()
        }

        binding.exitButton.setOnClickListener {
            finish()
        }
    }

    private fun showTimePicker(onTimeSelected: (hour: Int, minute: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                onTimeSelected(hourOfDay, minute)
            },
            currentHour,
            currentMinute,
            true // Use 24-hour format
        ).show()
    }

    private fun requestLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                    getLastKnownLocation()
                }
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                    getLastKnownLocation()
                }
                else -> {
                    Toast.makeText(this, "Location permission is required.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        locationPermissionRequest.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun getLastKnownLocation() {
        fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
            if (task.isSuccessful && task.result != null) {
                location = task.result

                val mapView = binding.mapView
                mapView.controller.setZoom(15.0)
                mapView.controller.setCenter(GeoPoint(location.latitude, location.longitude))
                val marker = Marker(mapView)
                marker.position = GeoPoint(location.latitude, location.longitude)
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.title = "Your location"
                mapView.overlays.add(marker)

            } else {
                Toast.makeText(this, "Failed to get location.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}