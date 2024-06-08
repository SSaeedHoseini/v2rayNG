package com.v2ray.ang.dto

import com.google.gson.annotations.SerializedName

data class APIError(
    @SerializedName("username")
    var username: List<String>?,
    @SerializedName("code")
    var code: List<String>?,
    @SerializedName("password")
    var password: List<String>?,
    @SerializedName("non_field_errors")
    var nonFieldErrors: List<String>?,
    @SerializedName("detail")
    var detail: String?,
)