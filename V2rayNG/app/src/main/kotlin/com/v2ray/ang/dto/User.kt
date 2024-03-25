package com.v2ray.ang.dto


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("is_staff")
    var isStaff: Boolean,
    @SerializedName("pk")
    var id: Int,
    @SerializedName("profile")
    var profile: Profile,
    @SerializedName("username")
    var username: String
)
