package com.v2ray.ang.connection

import com.google.gson.Gson
import com.v2ray.ang.AngApplication
import com.v2ray.ang.R
import com.v2ray.ang.dto.APIError
import retrofit2.HttpException
import java.io.IOException

fun getErrorMessage(th: Throwable): Exception {
    return when (th) {
        is IOException -> {
            Exception(AngApplication.application
                .getString(R.string.connection_test_fail))
        }
        is HttpException -> {
            if (th.message!!.contains("401")) {
                return FORCEException(convertJsonError(th.response()!!.errorBody()!!.string()))
            }
            return Exception(convertJsonError(th.response()!!.errorBody()!!.string()))
        }
        else -> {
            Exception(AngApplication.application
                .getString(R.string.unknown_error))
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
    if (e.detail != null){
        error += e.detail!! + "\n"}
    return error
}