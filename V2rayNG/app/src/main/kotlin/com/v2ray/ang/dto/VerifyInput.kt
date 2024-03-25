package com.v2ray.ang.dto


import com.google.gson.annotations.SerializedName

data class VerifyInput(
    @SerializedName("token")
    var token: String,
    @SerializedName("code")
    var code: String
)