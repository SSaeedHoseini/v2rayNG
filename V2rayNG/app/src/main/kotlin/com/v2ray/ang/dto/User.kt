package com.v2ray.ang.dto

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("pk")
    val id: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("userName")
    val username: String,

)
