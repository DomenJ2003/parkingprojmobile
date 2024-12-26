package com.example.parkingprojmobile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingprojmobile.databinding.ItemParkingBinding
import com.example.parkingprojmobile.mapUtil.MarkerParser

class ParkingAdapter(
    private val events: List<ParkingState>,
    private val onEventClick: (ParkingState) -> Unit,
    private val activity: AppCompatActivity
) : RecyclerView.Adapter<ParkingAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemParkingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(parking: ParkingState, onEventClick: (ParkingState) -> Unit, activity: AppCompatActivity) {
            binding.parkingStatusImage.setImageDrawable(MarkerParser.getMarkerColor(parking.openFreeParking, parking.openPaidParking, activity))
            binding.parkingTown.text = "aaa" // TODO: parking.town
            binding.endOfParking.text = "${parking.hour}:${parking.minute}"
            binding.root.setOnClickListener { onEventClick(parking) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemParkingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events[position], onEventClick, activity)
    }

    override fun getItemCount() = events.size
}