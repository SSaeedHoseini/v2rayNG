package com.v2ray.ang.dto


import com.google.gson.annotations.SerializedName
import java.lang.reflect.TypeVariable

data class LoginResponse(
    @SerializedName("access_token")
    var accessToken: String,
    @SerializedName("refresh_token")
    var refreshToken: String,
    @SerializedName("user")
    var user: User
)