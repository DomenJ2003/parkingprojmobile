package com.example.parkingprojmobile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingprojmobile.data.ParkingState
import com.example.parkingprojmobile.databinding.ItemParkingBinding
import com.example.parkingprojmobile.mapUtil.MarkerParser

class ParkingAdapter(
    private val events: MutableList<ParkingState>,
    private val activity: AppCompatActivity,
    private val onEventClick: (ParkingState) -> Unit,
) : RecyclerView.Adapter<ParkingAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemParkingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(parking: ParkingState, onEventClick: (ParkingState) -> Unit, activity: AppCompatActivity) {
            binding.parkingStatusImage.setImageDrawable(MarkerParser.getMarkerColor(parking.openFreeParking, parking.openPaidParking, activity))
            binding.parkingTown.text = parking.townName
            binding.endOfParking.text = "${parking.hour} : ${parking.minute}"
            binding.root.setOnClickListener { onEventClick(parking) }
        }
    }
    fun updateData(newParkings: List<ParkingState>) {
        events.clear()
        events.addAll(newParkings)
        notifyDataSetChanged()
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