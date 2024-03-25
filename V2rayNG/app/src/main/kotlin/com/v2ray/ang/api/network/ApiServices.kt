package com.v2ray.ang.api.network

import com.v2ray.ang.dto.*
import com.v2ray.ang.util.MmkvManager
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @POST("auth/login/")
    @Headers("Content-Type: application/json")
    fun login(@Body loginInput: LoginInput): Call<LoginResponse>

    @POST("auth/token/refresh/")
    @Headers("Content-Type: application/json")
    fun refreshToken(@Body refreshInput: RefreshInput): Call<RefreshResponse>

    @POST("auth/token/verify/")
    @Headers("Content-Type: application/json")
    fun getVerify(@Body verifyInput: VerifyInput): Call<Void>

    @GET("servers/config/")
    @Headers("withAuth:true", "Content-Type: application/json")
    fun getConfigs(): Call<ConfigResponse>

    @Headers("withAuth:true", "Content-Type: application/json")
    @GET("profiles/info/")
    fun getUser(): Call<User>
}