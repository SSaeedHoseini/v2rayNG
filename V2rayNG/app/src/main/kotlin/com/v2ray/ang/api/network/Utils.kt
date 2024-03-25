package com.v2ray.ang.api.network

import com.google.gson.Gson
import com.v2ray.ang.AngApplication
import com.v2ray.ang.R
import com.v2ray.ang.dto.APIError
import retrofit2.HttpException
import java.io.IOException

fun getErrorMessage(th: Throwable, message: String): String {
    return when (th) {
        is IOException -> {
            AngApplication.application
                .getString(R.string.server_is_not_available)
        }
        is HttpException -> {
            convertJsonError(message)
        }
        else -> {
            AngApplication.application
                .getString(R.string.an_unknown_error_occurred)
        }
    }
}

fun convertJsonError(message: String): String {
    var error = ""
    val e = Gson().fromJson(
        message,
        APIError::class.java
    )
    e.code?.forEach {
        error += it + "\n"
    }
    e.password?.forEach {
        error += it + "\n"
    }
    e.username?.forEach {
        error += it + "\n"
    }
    e.nonFieldErrors?.forEach {
        error += it + "\n"
    }
    return error
}