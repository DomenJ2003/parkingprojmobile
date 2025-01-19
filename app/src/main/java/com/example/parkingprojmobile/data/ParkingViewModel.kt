package com.example.parkingprojmobile.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkingprojmobile.api.ApiUtil
import com.example.parkingprojmobile.data.ParkingState

class ParkingViewModel(private val apiUtil: ApiUtil) : ViewModel() {
    private val _parkings = MutableLiveData<List<ParkingState>?>()
    val parkings: MutableLiveData<List<ParkingState>?> = _parkings

    fun fetchActiveEvents() {
        apiUtil.getMyParkings { parkings ->
            _parkings.postValue(parkings?.toList() ?: emptyList())
        }
    }

    fun fetchHistoryEvents() {
        apiUtil.getMyParkingsHistory { parkings ->
            _parkings.postValue(parkings?.toList() ?: emptyList())
        }
    }

    fun finishParking(parkingId: String) {
        apiUtil.finishParking(parkingId) {
            val updatedParkings = _parkings.value?.filter { it._id != parkingId }
            _parkings.postValue(updatedParkings)
        }
    }
}
