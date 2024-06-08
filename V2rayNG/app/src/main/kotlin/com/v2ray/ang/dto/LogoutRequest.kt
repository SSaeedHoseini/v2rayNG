package com.v2ray.ang.dto

import com.google.gson.annotations.SerializedName

data class LogoutRequest(
    @SerializedName("device_id")
    var deviceId: String,
)