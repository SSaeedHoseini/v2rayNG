package com.v2ray.ang.connection

import com.v2ray.ang.BuildConfig
import com.v2ray.ang.dto.*
import com.v2ray.ang.util.MmkvManager

class Repository(private val apiService: APIService) {

    suspend fun getConfigs(): List<Config> {
        try {
            return apiService.getConfigs()
        } catch (e: Exception) {
            throw getErrorMessage(e)
        }
    }

    suspend fun getUser(): User {
        try {
            val deviceId = MmkvManager.getDeviceId()
            val registrationId = MmkvManager.getRegistrationId()
            val version = BuildConfig.VERSION_NAME
            return apiService.getUser(
                DeviceRequest(
                    deviceId,
                    registrationId,
                    version
                )
            )
        } catch (e: Exception) {
            throw getErrorMessage(e)
        }
    }

    suspend fun login(username: String, password: String): Token {
        try {
            val deviceId = MmkvManager.getDeviceId()
            val registrationId = MmkvManager.getRegistrationId()
            val version = BuildConfig.VERSION_NAME
            val loginRequest = LoginRequest(
                username,
                password,
                deviceId,
                registrationId,
                version
            )
            return apiService.login(loginRequest)
        } catch (e: Exception) {
            throw getErrorMessage(e)
        }
    }

    suspend fun logout(): LogoutResponse {
        try {
            val device = MmkvManager.getDeviceId()
            return apiService.logout(LogoutRequest(device))
        } catch (e: Exception) {
            throw getErrorMessage(e)
        }
    }

    suspend fun registrationTokenUpdate() {
        try {
            val deviceId = MmkvManager.getDeviceId()
            val registrationId = MmkvManager.getRegistrationId()
            val version = BuildConfig.VERSION_NAME
            apiService.registrationTokenUpdate(
                DeviceRequest(
                    deviceId,
                    registrationId,
                    version
                )
            )
        } catch (e: Exception) {
            throw getErrorMessage(e)
        }
    }
}