package com.example.parkingprojmobile.api

import android.content.Context
import com.auth0.android.jwt.JWT
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class AuthProvider(private val context: Context) {

    companion object {
        private const val LOGIN_ENDPOINT = "/auth/login"
        private const val SIGNUP_ENDPOINT = "/auth/register"
        private const val PREFS_NAME = "auth_prefs"
        private const val TOKEN_KEY = "jwt_token"
    }

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val client = OkHttpClient()

    fun login(email: String, password: String, callback: (success: Boolean, error: String?) -> Unit) {
        val jsonBody = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(Constants.API_URL + LOGIN_ENDPOINT)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody ?: "")
                    val token = jsonResponse.optString("token", null)
                    if (token != null) {
                        saveToken(token)
                        callback(true, null)
                    } else {
                        callback(false, "Token not found in response")
                    }
                } else {
                    callback(false, "Login failed: ${response.message}")
                }
            }
        })
    }

    fun signup(username: String, email: String, password: String, callback: (success: Boolean, error: String?) -> Unit) {
        val jsonBody = JSONObject().apply {
            put("username", username)
            put("email", email)
            put("password", password)
        }

        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonBody.toString())
        val request = Request.Builder()
            .url(Constants.API_URL + SIGNUP_ENDPOINT)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody ?: "")
                    val token = jsonResponse.optString("token", null)
                    if (token != null) {
                        saveToken(token)
                        callback(true, null)
                    } else {
                        callback(false, "Token not found in response")
                    }
                } else {
                    callback(false, "Signup failed: ${response.message}")
                }
            }
        })
    }

    private fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    fun clearToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
    }

    fun getName(): String? {
        val jwtTokenString = sharedPreferences.getString(TOKEN_KEY, null) ?: return ""
        val jwt = JWT(jwtTokenString)
        return jwt.getClaim("username").asString()
    }

    fun isTokenValid(): Boolean {
        try {
            val jwtTokenString = sharedPreferences.getString(TOKEN_KEY, null) ?: return false
            val jwt = JWT(jwtTokenString)
            return !jwt.isExpired(0)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}
