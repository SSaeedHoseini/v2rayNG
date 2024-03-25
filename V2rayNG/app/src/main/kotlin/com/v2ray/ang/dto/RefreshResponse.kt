package com.v2ray.ang.dto


import com.google.gson.annotations.SerializedName

data class RefreshResponse(
    @SerializedName("access")
    var access: String,
    @SerializedName("refresh")
    var refresh: String,
    @SerializedName("access_token_expiration")
    var accessTokenExpiration: String
)