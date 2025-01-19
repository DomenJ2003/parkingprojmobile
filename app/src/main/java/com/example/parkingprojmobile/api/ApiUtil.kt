package com.example.parkingprojmobile.api

import android.content.Context
import com.example.parkingprojmobile.data.LiveCameraData
import com.example.parkingprojmobile.data.Parking
import com.example.parkingprojmobile.data.ParkingState
import okhttp3.*
import java.io.IOException
import com.google.gson.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.reflect.Type

class ApiUtil(private val context: Context) {

    companion object {
        private const val MY_PARKINGS = "/parking-state/my"
        private const val MY_PARKINGS_HISTORY = "/parking-state/my-history"
        private const val FINISH_PARKING = "/parking-state/finish/"
        private const val LIVE_DATA = "/computer-vision/data"
        private const val ALL_PARKING = "/parking/mobile"
        private const val PREFS_NAME = "auth_prefs"
        private const val TOKEN_KEY = "jwt_token"
    }

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val client = OkHttpClient()
    val gson = GsonBuilder()
        .registerTypeAdapter(Boolean::class.java, BooleanDeserializer())
        .create()

    fun getMyParkings(callback: (parkings: Array<ParkingState>?) -> Unit) {
        val token = sharedPreferences.getString(TOKEN_KEY, "") ?: ""
        val request = Request.Builder()
            .url(Constants.API_URL + MY_PARKINGS)
            .header("Authorization", "Bearer $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to get my parkings: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    callback(gson.fromJson(responseBody, Array<ParkingState>::class.java))
                } else {
                    println("Failed to get my parkings: ${response.message}")
                }
            }
        })
    }

    fun getMyParkingsHistory(callback: (parkings: Array<ParkingState>?) -> Unit) {
        val token = sharedPreferences.getString(TOKEN_KEY, "") ?: ""
        val request = Request.Builder()
            .url(Constants.API_URL + MY_PARKINGS_HISTORY)
            .header("Authorization", "Bearer $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to get my parkings: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    callback(gson.fromJson(responseBody, Array<ParkingState>::class.java))
                } else {
                    println("Failed to get my parkings: ${response.message}")
                }
            }
        })
    }



    fun finishParking(parkingId: String, callback: (success: Boolean) -> Unit) {
        val token = sharedPreferences.getString(TOKEN_KEY, "") ?: ""
        val request = Request.Builder()
            .url(Constants.API_URL + FINISH_PARKING + parkingId)
            .header("Authorization", "Bearer $token")
            .post("".toRequestBody(null))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to finish parking: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    println("Failed to finish parking: ${response.message}")
                }
            }
        })
    }

    fun getLiveParkingData(callback: (parkings: LiveCameraData) -> Unit) {
        val token = sharedPreferences.getString(TOKEN_KEY, "") ?: ""
        val request = Request.Builder()
            .url(Constants.API_URL + LIVE_DATA)
            .header("Authorization", "Bearer $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to live data: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    callback(gson.fromJson(responseBody, LiveCameraData::class.java))
                } else {
                    println("Failed to live data: ${response.message}")
                }
            }
        })
    }

    fun getAllParkings(callback: (parkings: Array<Parking>) -> Unit) {
        val token = sharedPreferences.getString(TOKEN_KEY, "") ?: ""
        val request = Request.Builder()
            .url(Constants.API_URL + ALL_PARKING)
            .header("Authorization", "Bearer $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to live data: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    callback(gson.fromJson(responseBody, Array<Parking>::class.java))
                } else {
                    println("Failed to live data: ${response.message}")
                }
            }
        })
    }
}

class BooleanDeserializer : JsonDeserializer<Boolean> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Boolean {
        return when {
            json.isJsonPrimitive && json.asJsonPrimitive.isNumber -> json.asInt != 0
            json.isJsonPrimitive && json.asJsonPrimitive.isBoolean -> json.asBoolean
            else -> throw JsonParseException("Unexpected type for boolean: ${json.javaClass}")
        }
    }
}