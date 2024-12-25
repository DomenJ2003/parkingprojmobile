package com.example.parkingprojmobile

data class ParkingState (
    val lat: Double,
    val lon: Double,
    val openFreeParking: Boolean,
    val openPaidParking: Boolean,
    val hour: Int,
    val minute: Int
)