package com.v2ray.ang.api.network

import com.google.gson.annotations.SerializedName
import com.v2ray.ang.dto.*
import com.v2ray.ang.util.MmkvManager
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @POST("auth/login/")
    @Headers("Content-Type: application/json")
    fun login(@Body payload: LoginInput): Call<LoginResponse>

    @GET("servers/config/")
    @Headers("withAuth:true", "Content-Type: application/json")
    fun getConfigs(): Call<ConfigResponse>

    @Headers("withAuth:true", "Content-Type: application/json")
    @GET("profiles/info/")
    fun getUser(): Call<User>
}
