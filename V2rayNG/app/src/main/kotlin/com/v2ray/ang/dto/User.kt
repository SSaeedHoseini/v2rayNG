package com.v2ray.ang.dto

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("global_message")
    val globalMessage: String?,
    @SerializedName("private_message")
    val privateMessage: String?,
    @SerializedName("usage")
    val usage: String?,
    @SerializedName("remained")
    val remained: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("debt")
    val debt: String?,
)
data class User(
    val username: String,
    @SerializedName("profile")
    val profile: Profile,

)
