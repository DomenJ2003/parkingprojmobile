package com.example.parkingprojmobile.api

import com.auth0.android.jwt.JWT

object JwtUtil {
    fun decodeToken(token: String): Map<String, Any> {
        val jwt = JWT(token)
        return jwt.claims
    }

    fun getName(token: String): String? {
        val jwt = JWT(token)
        return jwt.getClaim("username").asString()
    }

    fun getUserId(token: String): String? {
        val jwt = JWT(token)
        return jwt.getClaim("userId").asString()
    }

    fun isTokenValid(token: String): Boolean {
        try {
            val jwt = JWT(token)
            return !jwt.isExpired(0)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}