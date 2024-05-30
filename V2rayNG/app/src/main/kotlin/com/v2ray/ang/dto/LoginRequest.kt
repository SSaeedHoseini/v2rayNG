package com.v2ray.ang.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username")
    var userName: String,

    @SerializedName("password")
    var passWord: String
)