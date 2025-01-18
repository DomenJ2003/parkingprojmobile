package com.example.parkingprojmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkingprojmobile.api.ApiUtil
import com.example.parkingprojmobile.data.ParkingState
import com.example.parkingprojmobile.databinding.ActivityParkingListBinding

class ParkingListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParkingListBinding
    private val parkings = mutableListOf<ParkingState>()
    private lateinit var apiUtil: ApiUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParkingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener{
            finish()
        }
        apiUtil = ApiUtil(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        fetchEvents()
    }

    private fun fetchEvents() {

        apiUtil.getMyParkings() { parkings ->
            runOnUiThread {
                this.parkings.clear()
                parkings?.forEach { parking ->
                    this.parkings.add(parking)
                }
                binding.recyclerView.adapter = ParkingAdapter(
                    this.parkings,
                    this,
                ) { parking ->
                    StopDialog {
                        apiUtil.finishParking(parking._id) {
                            val np = this.parkings.filter { it._id != parking._id }
                            this.parkings.clear()
                            this.parkings.addAll(np)
                        }
                        println("OK button pressed ${parking._id}")
                    }.show(supportFragmentManager, "StopDialog")
                }
            }
        }

    }

}

