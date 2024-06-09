package com.v2ray.ang.dto

import com.google.gson.annotations.SerializedName

data class DeviceRequest(
    @SerializedName("device_id")
    var deviceId: String,

    @SerializedName("registration_id")
    var registrationId: String,

    @SerializedName("version")
    var version: String,

)