package com.v2ray.ang.connection

import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Body

import com.v2ray.ang.dto.*
import retrofit2.Response

interface APIService {
    @GET("/api/configs/")
    suspend fun getConfigs(): List<Config>

    @GET("/api/user/detail/")
    suspend fun getUser(): User

    @POST("/api/user/login/")
    suspend fun login(@Body loginRequest: LoginRequest): Token

    @POST("/api/user/logout/")
    suspend fun logout(@Body logoutRequest:LogoutRequest): LogoutResponse

    @POST("/api/user/token-update/")
    suspend fun registrationTokenUpdate(@Body deviceRequest:DeviceRequest): Response<Unit>
}