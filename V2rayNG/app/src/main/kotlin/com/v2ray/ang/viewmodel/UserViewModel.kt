package com.v2ray.ang.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.v2ray.ang.AngApplication
import com.v2ray.ang.api.interfaces.NetworkResponseCallback
import com.v2ray.ang.api.repositories.UserRepository
import com.v2ray.ang.helper.NetworkHelper

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var isLogin: MutableLiveData<Boolean>
    private var mRepository = UserRepository.getInstance()

    val mShowProgressBar = MutableLiveData(true)
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowAPIError = MutableLiveData<String>()

    fun login(username: String, password: String): MutableLiveData<Boolean> {
        if (NetworkHelper.isOnline(AngApplication.application)) {
            mShowProgressBar.value = true
            isLogin = mRepository.loginUser(object : NetworkResponseCallback {
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
        return isLogin
    }
}