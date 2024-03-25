package com.v2ray.ang.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.v2ray.ang.AngApplication
import com.v2ray.ang.api.interfaces.NetworkResponseCallback
import com.v2ray.ang.dto.ConfigResponse
import com.v2ray.ang.helper.NetworkHelper
import com.v2ray.ang.util.AngConfigManager
import com.v2ray.ang.util.Utils
import com.v2ray.ang.api.repositories.ConfigsRepository

class ConfigsViewModel(application: Application) : AndroidViewModel(application) {

    private var config: MutableLiveData<ConfigResponse> = MutableLiveData<ConfigResponse>()
    private var mRepository = ConfigsRepository.getInstance()

    val mShowProgressBar = MutableLiveData(true)
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowAPIError = MutableLiveData<String>()

    fun getConfigs(forceFetch: Boolean = false): MutableLiveData<ConfigResponse> {
        if (NetworkHelper.isOnline(AngApplication.application)) {
            mShowProgressBar.value = true
            config = mRepository.getConfigs(object : NetworkResponseCallback {
                override fun onResponseSuccess() {
                    mShowProgressBar.value = false
                }

                override fun onResponseFailure(th: Throwable) {
                    mShowAPIError.value = th.message
                    mShowProgressBar.value = false
                }
            }, forceFetch)
        } else {
            mShowNetworkError.value = true
        }
        return config
    }

    fun importConfig(server: String) {
        var count = AngConfigManager.importBatchConfig(server, "", true)
        if (count <= 0) {
            AngConfigManager.importBatchConfig(Utils.decode(server!!), "", true)
        }
    }
}