package com.v2ray.ang.dto


import com.google.gson.annotations.SerializedName

data class ConfigResponse(
    @SerializedName("urls")
    var urls: List<String>
)