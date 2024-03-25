package com.v2ray.ang.api.repositories

import androidx.lifecycle.MutableLiveData
import com.v2ray.ang.api.interfaces.NetworkResponseCallback
import com.v2ray.ang.api.network.RestClient
import com.v2ray.ang.api.network.convertJsonError
import com.v2ray.ang.api.network.getErrorMessage
import com.v2ray.ang.dto.*
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ConfigsRepository private constructor() {
    private lateinit var mCallback: NetworkResponseCallback

    private var config: MutableLiveData<ConfigResponse> =
        MutableLiveData<ConfigResponse>()

    private lateinit var configCall: Call<ConfigResponse>


    fun getConfigs(
        callback: NetworkResponseCallback,
        forceFetch: Boolean = false
    ): MutableLiveData<ConfigResponse> {
        mCallback = callback
        if (config.value != null && !forceFetch) {
            mCallback.onResponseSuccess()
            return config
        }
        configCall = RestClient.getInstance().getApiService().getConfigs()
        configCall.enqueue(object : Callback<ConfigResponse> {
            override fun onResponse(
                call: Call<ConfigResponse>,
                response: Response<ConfigResponse>
            ) {
                if (response.isSuccessful) {
                    config.value = response.body()
                    mCallback.onResponseSuccess()
                } else {
                    mCallback.onResponseFailure(
                        try {
                            Throwable(
                                convertJsonError(response.errorBody()!!.string())
                            )
                        } catch (e: Exception) {
                            Throwable(
                                response.message()
                            )
                        }
                    )
                }

            }

            override fun onFailure(call: Call<ConfigResponse>, th: Throwable) {
                if (th !is IOException) {
                    mCallback.onResponseFailure(Throwable(th.message?.let {
                        getErrorMessage(
                            th,
                            it
                        )
                    }))
                }
            }

        })
        return config
    }

    companion object {
        private var mInstance: ConfigsRepository? = null
        fun getInstance(): ConfigsRepository {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = ConfigsRepository()
                }
            }
            return mInstance!!
        }
    }

}