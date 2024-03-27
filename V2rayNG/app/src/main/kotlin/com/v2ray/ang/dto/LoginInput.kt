package com.v2ray.ang.dto


import com.google.gson.annotations.SerializedName

data class LoginInput(
    @SerializedName("username")
    var username: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("device_id")
    var deviceId: String,
    @SerializedName("registration_id")
    var registrationId: String,
    @SerializedName("version")
    var version: String,
)