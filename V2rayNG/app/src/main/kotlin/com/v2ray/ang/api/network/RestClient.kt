package com.v2ray.ang.api.network

import com.v2ray.ang.AngApplication
import com.v2ray.ang.BuildConfig
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.*
import retrofit2.Retrofit


class RestClient {

    companion object {
//        private const val BASE_URL = "https://shahid-aleali.site/"
        private const val BASE_URL = "http://192.168.1.104:8000/"

        private lateinit var mApiServices: ApiServices
        private var mInstance: RestClient? = null
        fun getInstance(): RestClient {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = RestClient()
                }
            }
            return mInstance!!
        }
    }

    init {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(AuthInterceptor(AngApplication.application))
        if (BuildConfig.DEBUG) okHttpClient.addInterceptor(loggingInterceptor())
        okHttpClient.connectTimeout(15, TimeUnit.SECONDS)
        okHttpClient.readTimeout(15, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(30, TimeUnit.SECONDS)
        okHttpClient.retryOnConnectionFailure(true)
        okHttpClient.connectionPool(ConnectionPool())
        okHttpClient.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        mApiServices = retrofit.create(ApiServices::class.java)
    }

    fun getApiService() = mApiServices
}