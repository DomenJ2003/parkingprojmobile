package com.example.parkingprojmobile

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingprojmobile.api.ApiUtil
import com.example.parkingprojmobile.data.Parking
import com.example.parkingprojmobile.databinding.ActivityMapBinding
import com.example.parkingprojmobile.mapUtil.MapHelper
import com.example.parkingprojmobile.mapUtil.MarkerParser
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

class MapActivity: AppCompatActivity() {
    lateinit var binding: ActivityMapBinding
    lateinit var mapView: MapView
    lateinit var mapHelper: MapHelper
    lateinit var apiUtil: ApiUtil
    lateinit var allParkings: List<Parking>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = binding.mapView

        mapHelper = MapHelper( this, mapView)
        mapHelper.initMapToMyLocation(false)

        apiUtil = ApiUtil(this)

        apiUtil.getAllParkings { parkings ->
            allParkings = parkings.toList()
            initAllParkings()
        }

        val parkingStateList = (application as MyApplication).parkingStateList

        parkingStateList.forEach {
            mapHelper.addMarker(MarkerParser.parseMarkerFromMessage(it, mapView, this))
        }

        binding.ljButton.setOnClickListener {
            mapHelper.setMapPosition(46.0569, 14.5058, 14.0)
        }

        binding.mbButton.setOnClickListener {
            mapHelper.setMapPosition(46.5547, 15.6459)
        }

        binding.nmButton.setOnClickListener {
            mapHelper.setMapPosition(45.8030, 15.1609, 18.0)
        }

        binding.backButton.setOnClickListener {
            finish()
        }

    }

    private fun initAllParkings(){

        val markerList: MutableList<Marker> = mutableListOf<Marker>()
        val polygonList: MutableList<Polygon> = mutableListOf<Polygon>()

        allParkings.forEach {
            if(it.geometry.coordinates.size<2){
                markerList.add(parseMarker(it))
            } else {
                polygonList.add(parsePolygon(it))
            }
        }

        mapHelper.addPolygons(polygonList)
        mapHelper.addMarkers(markerList)
    }

    private fun parsePolygon(parking: Parking): Polygon{
        val polygon: Polygon = Polygon(mapView)
        val polygonPoints = ArrayList<GeoPoint>()

        parking.geometry.coordinates.forEach {
            polygonPoints.add(GeoPoint(it[1], it[0]))
        }

        polygon.points = polygonPoints
        polygon.title = generateParkingDescription(parking)
        polygon.fillColor = getFillColor(parking)
        polygon.strokeColor = getStrokeColor(parking)
        polygon.strokeWidth = 5.0f
        return polygon
    }

    private fun parseMarker(parking: Parking): Marker{
        val marker: Marker = Marker(binding.mapView)
        marker.position = GeoPoint(parking.geometry.coordinates[0][1], parking.geometry.coordinates[0][0])
        marker.title = generateParkingDescription(parking)
        marker.icon = getMarkerIcon(parking)
        return marker
    }


    private fun generateParkingDescription(parking: Parking): String {
        val builder = StringBuilder()

        builder.append(getString(R.string.parking)).append(parking.name)
        builder.append(getString(R.string.in_town)).append(parking.town.name)
        builder.append(getString(R.string.post_code)).append(parking.town.postNumber)

        if (parking.availableSpaces.toInt() != -1) {
            builder.append(getString(R.string.available_spaces)).append(parking.availableSpaces)
        }
        if (parking.freeSpaces.toInt() != -1) {
            builder.append(getString(R.string.free_spaces)).append(parking.freeSpaces)
        }
        if (parking.disabledParkingSpaces.toInt() != -1) {
            builder.append(getString(R.string.disabled_parking_spaces)).append(parking.disabledParkingSpaces)
        }
        if (parking.price.isNotEmpty()) {
            builder.append(getString(R.string.price)).append(parking.price)
        }
        if (parking.cona.isNotEmpty()) {
            builder.append(getString(R.string.cona)).append(parking.cona)
        }

        return builder.toString()
    }

    private fun getMarkerIcon(parking: Parking): Drawable{
        if(parking.freeSpaces.toInt() == 0){
            return this.resources.getDrawable(R.drawable.ic_marker_red, null)
        }
        if(parking.availableSpaces.toInt() != 0){
            return this.resources.getDrawable(R.drawable.ic_marker_green, null)
        }
        if(parking.disabledParkingSpaces.toInt() != 0) {
            return this.resources.getDrawable(R.drawable.ic_marker_blue, null)
        }
        return this.resources.getDrawable(R.drawable.ic_marker_black, null)
    }

    private fun getFillColor(parking: Parking): Int {
        if(parking.freeSpaces.toInt() == 0){
            return 0x12FF1212
        }
        return 0x1212FF12
    }

    private fun getStrokeColor(parking: Parking): Int {
        if(parking.freeSpaces.toInt() == 0){
            return 0xFFFF0000.toInt()
        }
        return 0xFF00FF00.toInt()

    }

}