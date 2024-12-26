package com.example.parkingprojmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkingprojmobile.databinding.ActivityParkingListBinding

class ParkingListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParkingListBinding
    private val parkings = mutableListOf<ParkingState>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParkingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener{
            finish()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        fetchEvents()
    }

    private fun fetchEvents() {
//        firestore.collection("events")
//            .get()
//            .addOnSuccessListener { result ->
//                events.clear()
//                println(result.documents.size)
//                println()
//                for (document in result) {
//                    val event = document.toObject(Event::class.java)
//                    events.add(event)
//                }
//                binding.recyclerView.adapter = ParkingAdapter(events) { event ->
//                    openEventDetails(event)
//                }
//            }
//            .addOnFailureListener { e ->
//                e.printStackTrace()
//            }
    }

}

