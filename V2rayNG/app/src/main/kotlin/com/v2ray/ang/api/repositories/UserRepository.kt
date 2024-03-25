package com.v2ray.ang.api.repositories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tencent.mmkv.MMKV
import com.v2ray.ang.BuildConfig
import com.v2ray.ang.api.interfaces.NetworkResponseCallback
import com.v2ray.ang.api.network.RestClient
import com.v2ray.ang.api.network.convertJsonError
import com.v2ray.ang.api.network.getErrorMessage
import com.v2ray.ang.dto.*
import com.v2ray.ang.extension.toast
import com.v2ray.ang.util.MmkvManager
import com.v2ray.ang.util.MmkvManager.getDeviceId
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await


class UserRepository private constructor() {
    private lateinit var mCallback: NetworkResponseCallback

    private var loginUser: MutableLiveData<LoginResponse?> =
        MutableLiveData<LoginResponse?>()
    private var user: MutableLiveData<User> =
        MutableLiveData<User>()
    private var userVerified: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>()

    private lateinit var loginUserCall: Call<LoginResponse>
    private lateinit var userCall: Call<User>
    private lateinit var userVerifiedCall: Call<Void>

    fun loginUser(
        callback: NetworkResponseCallback,
        username: String,
        password: String
    ): MutableLiveData<LoginResponse?> {
        mCallback = callback
        if (loginUser.value != null) {
            mCallback.onResponseSuccess()
            return loginUser
        }
        loginUserCall = RestClient.getInstance().getApiService()
            .login(
                LoginInput(
                    username = username,
                    password = password,
                    code = getDeviceId(),
                    version = BuildConfig.VERSION_NAME
                )
            )
        loginUserCall.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    MmkvManager.encodeAccount(response.body()!!)
                    loginUser.value = response.body()
                    mCallback.onResponseSuccess()
                } else {
                    loginUser.value = null
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
                    loginUser.value = null
                    mCallback.onResponseFailure(Throwable(th.message?.let {
                        getErrorMessage(
                            th,
                            it
                        )
                    }))
                }
            }
        })
        return loginUser
    }


    fun getUser(
        callback: NetworkResponseCallback,
        forceFetch: Boolean
    ): MutableLiveData<User> {
        mCallback = callback
        if (user.value != null && !forceFetch) {
            mCallback.onResponseSuccess()
            return user
        }
        userCall = RestClient.getInstance().getApiService().getUser()
        userCall.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    user.value = response.body()
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

            override fun onFailure(call: Call<User>, th: Throwable) {
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
        return user
    }

    fun getVerify(
        callback: NetworkResponseCallback,
        forceFetch: Boolean = false
    ): MutableLiveData<Boolean> {
        mCallback = callback
        if (userVerified.value != null && !forceFetch) {
            mCallback.onResponseSuccess()
            return userVerified
        }
        val token = MmkvManager.decodeAccessToken()
        if (!token.isNullOrEmpty()) {
            userVerifiedCall = RestClient.getInstance().getApiService()
                .getVerify(VerifyInput(token = token, code = getDeviceId()))
            userVerifiedCall.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        mCallback.onResponseSuccess()
                        userVerified.value = true
                    } else {
                        userVerified.value = false
                        mCallback.onResponseFailure(
                            try {
                                Throwable(
                                    convertJsonError(response.errorBody()!!.string())
                                )
                            } catch (e: Exception) {
                                Throwable(
                                    convertJsonError(response.message())
                                )
                            }
                        )
                    }
                }

                override fun onFailure(call: Call<Void>, th: Throwable) {
                    userVerified.value = false
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
            return userVerified
        } else {
            userVerified.value = false
            return userVerified
        }
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