package com.v2ray.ang.api.repositories

import androidx.lifecycle.MutableLiveData
import com.v2ray.ang.BuildConfig
import com.v2ray.ang.api.interfaces.NetworkResponseCallback
import com.v2ray.ang.api.network.RestClient
import com.v2ray.ang.api.network.convertJsonError
import com.v2ray.ang.api.network.getErrorMessage
import com.v2ray.ang.dto.*
import com.v2ray.ang.util.MmkvManager
import com.v2ray.ang.util.MmkvManager.getDeviceId
import com.v2ray.ang.util.MmkvManager.getRegistrationId
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserRepository private constructor() {
    private lateinit var mCallback: NetworkResponseCallback

    private var isLogin: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>()
    private lateinit var loginUserCall: Call<LoginResponse>

    fun loginUser(
        callback: NetworkResponseCallback,
        username: String,
        password: String
    ): MutableLiveData<Boolean> {
        mCallback = callback
        if (isLogin.value != null) {
            mCallback.onResponseSuccess()
            return isLogin
        }
        val deviceId = getDeviceId()
        val registrationId = getRegistrationId()

        loginUserCall = RestClient.getInstance().getApiService()
            .login(
                LoginInput(
                    username = username,
                    password = password,
                    deviceId = deviceId ,
                    registrationId = registrationId,
                    version = BuildConfig.VERSION_NAME
                )
            )
        loginUserCall.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    MmkvManager.encodeToken(response.body()!!.key)
                    isLogin.value = true
                    mCallback.onResponseSuccess()
                } else {
                    isLogin.value = false
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

            override fun onFailure(call: Call<LoginResponse>, th: Throwable) {
                if (th !is IOException) {
                    isLogin.value = false
                    mCallback.onResponseFailure(Throwable(th.message?.let {
                        getErrorMessage(
                            th,
                            it
                        )
                    }))
                }
            }
        })
        return isLogin
    }


    companion object {
        private var mInstance: UserRepository? = null
        fun getInstance(): UserRepository {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = UserRepository()
                }
            }
            return mInstance!!
        }
    }

}