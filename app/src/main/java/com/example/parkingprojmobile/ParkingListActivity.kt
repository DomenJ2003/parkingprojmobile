package com.example.parkingprojmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkingprojmobile.api.ApiUtil
import com.example.parkingprojmobile.data.ParkingViewModel
import com.example.parkingprojmobile.data.ParkingViewModelFactory
import com.example.parkingprojmobile.databinding.ActivityParkingListBinding

class ParkingListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParkingListBinding
    private val parkingAdapter = ParkingAdapter(mutableListOf(), this) { parking ->
        StopDialog {
            parkingViewModel.finishParking(parking._id)
        }.show(supportFragmentManager, "StopDialog")
    }

    private lateinit var parkingViewModel: ParkingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParkingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiUtil = ApiUtil(this)
        parkingViewModel = ViewModelProvider(this, ParkingViewModelFactory(apiUtil))[ParkingViewModel::class.java]

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = parkingAdapter

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.activeBtn.setOnClickListener {
            parkingViewModel.fetchActiveEvents()
            binding.activeText.text = "Active"
        }

        binding.historyButton.setOnClickListener {
            parkingViewModel.fetchHistoryEvents()
            binding.activeText.text = "History"
        }

        observeViewModel()
        parkingViewModel.fetchActiveEvents()
    }

    private fun observeViewModel() {
        parkingViewModel.parkings.observe(this) { updatedParkings ->
            if (updatedParkings != null) {
                parkingAdapter.updateData(updatedParkings)
            }
        }
    }
}
