package com.v2ray.ang.dto


import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("private_message")
    var privateMessage: String?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("expire_at")
    var expireAt: String,
    @SerializedName("paid")
    var paid: Boolean
)