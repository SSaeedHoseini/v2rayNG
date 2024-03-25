package com.v2ray.ang.dto


import com.google.gson.annotations.SerializedName

data class LoginInput(
    @SerializedName("code")
    var code: String,
    @SerializedName("version")
    var version: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("username")
    var username: String
)