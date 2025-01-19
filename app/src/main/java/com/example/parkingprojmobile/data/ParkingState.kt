package com.example.parkingprojmobile.data

data class ParkingState (
    val _id: String,
    val lat: Double,
    val lon: Double,
    val openFreeParking: Boolean,
    val openPaidParking: Boolean,
    val hour: Int,
    val minute: Int,
    val townName: String = ""
)