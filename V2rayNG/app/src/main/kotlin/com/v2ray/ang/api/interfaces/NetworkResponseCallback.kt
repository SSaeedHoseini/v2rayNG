package com.v2ray.ang.api.interfaces

interface NetworkResponseCallback {

    fun onResponseSuccess()

    fun onResponseFailure(th: Throwable)
}