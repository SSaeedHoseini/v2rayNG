package com.v2ray.ang.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.v2ray.ang.AngApplication
import com.v2ray.ang.connection.Repository
import com.v2ray.ang.dto.Config
import com.v2ray.ang.dto.ConfigsResponse
import com.v2ray.ang.util.AngConfigManager
import com.v2ray.ang.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfigViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(AngApplication.apiService)

    private val _configsLiveData = MutableLiveData<List<Config>>()
    val configsLiveData: LiveData<List<Config>> = _configsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun getConfigs() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val configs = repository.getConfigs()
                _configsLiveData.postValue(configs)
            } catch (e: Exception) {
                _errorLiveData.postValue(e.message)
            }
        }
    }

    fun importConfig(server: String) {
        var count = AngConfigManager.importBatchConfig(server, "", true)
        if (count <= 0) {
            AngConfigManager.importBatchConfig(Utils.decode(server!!), "", true)
        }
    }

}