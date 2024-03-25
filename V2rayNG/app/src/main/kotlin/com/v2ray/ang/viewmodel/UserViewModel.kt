package com.v2ray.ang.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.v2ray.ang.AngApplication
import com.v2ray.ang.api.interfaces.NetworkResponseCallback
import com.v2ray.ang.api.repositories.UserRepository
import com.v2ray.ang.dto.LoginResponse
import com.v2ray.ang.dto.User
import com.v2ray.ang.helper.NetworkHelper

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var userProfile: MutableLiveData<User>
    private lateinit var userVerifiedCall: MutableLiveData<Boolean>
    private lateinit var user: MutableLiveData<LoginResponse?>
    private var mRepository = UserRepository.getInstance()

    val userVerified = MutableLiveData(true)
    val mShowProgressBar = MutableLiveData(true)
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowAPIError = MutableLiveData<String>()

    fun loginUser(username: String, password: String): MutableLiveData<LoginResponse?> {
        if (NetworkHelper.isOnline(AngApplication.application)) {
            mShowProgressBar.value = true
            user = mRepository.loginUser(object : NetworkResponseCallback {
                override fun onResponseSuccess() {
                    mShowProgressBar.value = false
                }

                override fun onResponseFailure(th: Throwable) {
                    mShowProgressBar.value = false
                    mShowAPIError.value = th.message
                }
            }, username, password)
        } else {
            mShowNetworkError.value = true
        }
        return user
    }

    fun getUser(forceFetch: Boolean): MutableLiveData<User> {
        if (NetworkHelper.isOnline(AngApplication.application)) {
            mShowProgressBar.value = true
            userProfile = mRepository.getUser(object : NetworkResponseCallback {
                override fun onResponseSuccess() {
                    mShowProgressBar.value = false
                }

                override fun onResponseFailure(th: Throwable) {
                    mShowAPIError.value = th.message
                }

            }, forceFetch)
        } else {
            mShowNetworkError.value = true
        }
        return userProfile
    }

    fun getVerify(forceFetch: Boolean = false): MutableLiveData<Boolean> {
        if (NetworkHelper.isOnline(AngApplication.application)) {
            mShowProgressBar.value = true
            userVerifiedCall = mRepository.getVerify(object : NetworkResponseCallback {
                override fun onResponseSuccess() {
                    mShowProgressBar.value = false
                    userVerified.value = true
                }

                override fun onResponseFailure(th: Throwable) {
                    mShowProgressBar.value = false
                    userVerified.value = false
                    mShowAPIError.value = th.message
                }

            }, forceFetch)
        } else {
            mShowNetworkError.value = true
        }
        return userVerified
    }
}