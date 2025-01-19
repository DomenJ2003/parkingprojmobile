package com.example.parkingprojmobile

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.parkingprojmobile.api.ApiUtil
import com.example.parkingprojmobile.data.LiveCameraData
import com.example.parkingprojmobile.databinding.ActivityLiveCameraBinding


class LiveCameraActivity: AppCompatActivity() {
    lateinit var binding: ActivityLiveCameraBinding
    lateinit var apiUtil: ApiUtil
    lateinit var liveData: LiveCameraData
    private val handler = Handler(Looper.getMainLooper())
    private val delay: Long = 1000
    private var videoSecond: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiUtil = ApiUtil(this)
        fetchData()

        binding.button.setOnClickListener {
            finish()
        }
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.video)
        binding.videoView.setVideoURI(videoUri)
        binding.videoView.setOnCompletionListener {
            binding.videoView.start()
        }
        binding.videoView.start()

    }

    private fun fetchData(){
        apiUtil.getLiveParkingData { parkings ->
            liveData = parkings
            startRepeatingTask()
        }
    }

    private fun startRepeatingTask() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (::liveData.isInitialized) {
                    updateVideoView()
                }
                handler.postDelayed(this, delay)
            }
        }, delay)
    }

    private fun updateVideoView() {
        videoSecond+=1
        binding.space0.text = getText(videoSecond, liveData.park0)
        binding.space1.text = getText(videoSecond, liveData.park1)
        binding.space2.text = getText(videoSecond, liveData.park2)
        binding.space3.text = getText(videoSecond, liveData.park3)
        binding.space4.text = getText(videoSecond, liveData.park4)
        binding.space5.text = getText(videoSecond, liveData.park5)
        binding.space6.text = getText(videoSecond, liveData.park6)
        binding.space7.text = getText(videoSecond, liveData.park7)
    }

    private fun getText(videoSec: Number, space: List<Number>): String {
        if(space.contains(videoSec)){
            return "Free"
        }
        return "Busy"
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}