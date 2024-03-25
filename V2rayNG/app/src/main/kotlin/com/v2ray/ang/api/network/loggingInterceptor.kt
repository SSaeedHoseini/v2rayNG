package com.v2ray.ang.api.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import okio.IOException
import android.util.Log
import okhttp3.ResponseBody.Companion.toResponseBody

class loggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        val t1 = System.nanoTime()
        Log.d(
            "request logging",
            "Sending request ${request.url} with ${request.headers}",
        )
        Log.d(
            "request logging",
            "REQUEST BODY BEGIN:\n ${bodyToString(request)}\nREQUEST BODY END"
        )
        val response = chain.proceed(request)

        val responseBody = response.body
        val responseBodyString = response.body!!.string()

        val newResponse = response.newBuilder().body(
            responseBodyString.toByteArray()
                .toResponseBody(responseBody!!.contentType())
        ).build()

        val t2 = System.nanoTime()
        Log.d(
            "request logging",
            "Received response with code ${response.code} for ${response.request.url} in ${(t2 - t1) / 1e6}.1fm ${response.headers}",
        )
        Log.d(
            "request logging",
            "RESPONSE BODY BEGIN:\n ${responseBodyString}\nRESPONSE BODY END"
        )

        return newResponse
    }

    private fun bodyToString(request: Request): String? {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            if (!copy.body?.toString().isNullOrEmpty()) {
                copy.body!!.writeTo(buffer)
                buffer.readUtf8()
            } else ""
        } catch (e: IOException) {
            "did not work"
        }
    }
}