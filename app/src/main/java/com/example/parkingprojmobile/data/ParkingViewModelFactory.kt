package com.example.parkingprojmobile.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parkingprojmobile.api.ApiUtil

class ParkingViewModelFactory(private val apiUtil: ApiUtil) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParkingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ParkingViewModel(apiUtil) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}