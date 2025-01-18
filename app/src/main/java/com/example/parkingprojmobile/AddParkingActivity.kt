package com.example.parkingprojmobile

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingprojmobile.data.ParkingState
import com.example.parkingprojmobile.databinding.ActivityAddParkingBinding
import com.example.parkingprojmobile.mapUtil.MapHelper
import com.google.gson.Gson



class AddParkingActivity: AppCompatActivity() {
    lateinit var binding: ActivityAddParkingBinding
    private lateinit var location: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mapHelper = MapHelper(this, binding.mapView)
        mapHelper.initMapToMyLocation()
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
            location = mapHelper.getLocation()

            val mqttProvider = (application as MyApplication).mqttProvider

            val parking = ParkingState(
                "",
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
}