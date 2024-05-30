package com.v2ray.ang.connection

import com.v2ray.ang.dto.*

class Repository(private val apiService: APIService) {

    suspend fun getConfigs(): List<Config> {
        try {
            return apiService.getConfigs()
        } catch (e: Exception) {
            throw Exception(e.message ?: "Unknown error")
        }
    }

    suspend fun getUser(): UserDetailResponse {
        try {
            return apiService.getUser()
        } catch (e: Exception) {
            throw Exception(e.message ?: "Unknown error")
        }
    }

    suspend fun login(username: String, password: String): Token {
        try {
            val loginRequest = LoginRequest(username, password)
            return apiService.login(loginRequest)
        } catch (e: Exception) {
            throw Exception(e.message ?: "Unknown error")
        }
    }

    suspend fun logout(): LogoutResponse {
        try {
            return apiService.logout()
        } catch (e: Exception) {
            throw Exception(e.message ?: "Unknown error")
        }
    }
}