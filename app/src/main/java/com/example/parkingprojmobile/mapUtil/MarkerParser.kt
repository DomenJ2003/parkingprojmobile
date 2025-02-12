package com.example.parkingprojmobile.mapUtil

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingprojmobile.data.ParkingState
import com.google.gson.Gson
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

object MarkerParser {

    fun parseMarkerState(marker: String): ParkingState? {
        val gson = Gson()
        var parkingState = gson.fromJson(marker, ParkingState::class.java)
        return parkingState
    }

    fun parseMarkerFromMessage(parkingState: ParkingState, mapView: MapView, activity: AppCompatActivity): Marker {
        val newMarker = Marker(mapView)
        newMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        newMarker.position.latitude = parkingState.lat
        newMarker.position.longitude = parkingState.lon
        newMarker.icon = getMarkerColor(parkingState.openFreeParking, parkingState.openPaidParking, activity)
        newMarker.title = getDescription(parkingState)

        return newMarker
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getMarkerColor(freeParking: Boolean, paidParking: Boolean, activity: AppCompatActivity): Drawable {
        return if (freeParking) {
            activity.resources.getDrawable(com.example.parkingprojmobile.R.drawable.ic_marker_green, null)
        } else if (paidParking) {
            activity.resources.getDrawable(com.example.parkingprojmobile.R.drawable.ic_marker_blue, null)
        } else {
            activity.resources.getDrawable(com.example.parkingprojmobile.R.drawable.ic_marker_red, null)
        }
    }

    private fun getDescription(parkingState: ParkingState): String {
        return if (parkingState.openFreeParking) {
            "Free parking\nTime: ${parkingState.hour}:${parkingState.minute}"
        } else if (parkingState.openPaidParking) {
            "Paid parking\n" +
                    "Time: ${parkingState.hour}:${parkingState.minute}"
        } else {
            "No parking\n" +
                    "Time: ${parkingState.hour}:${parkingState.minute}"
        }
    }
}