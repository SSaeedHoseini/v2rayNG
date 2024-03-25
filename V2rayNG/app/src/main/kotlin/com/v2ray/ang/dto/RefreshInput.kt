package com.v2ray.ang.dto


import com.google.gson.annotations.SerializedName

data class RefreshInput(
    @SerializedName("refresh")
    var refresh: String?
)