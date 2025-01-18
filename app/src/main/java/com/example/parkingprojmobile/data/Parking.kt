package com.example.parkingprojmobile.data

data class Parking (
    val name: String,
    val town: Town,
    val status: Number = -1,
    val availableSpaces: Number = -1,
    val freeSpaces: Number = -1,
    val disabledParkingSpaces: Number = -1,
    val cona: String = "",
    val price: String = "",
    val geometry: Geometry = Geometry()
)