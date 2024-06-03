package com.v2ray.ang.viewmodel

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.v2ray.ang.AngApplication
import com.v2ray.ang.R
import com.v2ray.ang.connection.Repository
import com.v2ray.ang.databinding.DialogUserLoginBinding
import com.v2ray.ang.dto.*
import com.v2ray.ang.util.MmkvManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(AngApplication.apiService)
    private val _userDetailsLiveData = MutableLiveData<User>()
    val userDetailLiveData: LiveData<User> = _userDetailsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val _loginLiveData = MutableLiveData<Boolean>()
    val loginLiveData: LiveData<Boolean> = _loginLiveData

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val logoutResponse = repository.logout()
                if (logoutResponse.detail.contains("Successfully logged out.")) {
                    _loginLiveData.postValue(false)
                }
            } catch (e: Exception) {
                _errorLiveData.postValue(e.message)
            }
        }
    }

    fun getUser() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = repository.getUser()
                MmkvManager.setUser(user)
                _userDetailsLiveData.postValue(user)
            } catch (e: Exception) {
                var user = MmkvManager.getUser()
                if (user != null) _userDetailsLiveData.postValue(user!!)
                _errorLiveData.postValue(e.message)
            }
        }
    }

    fun login(context: Context) {
        val ivBinding = DialogUserLoginBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(context).setView(ivBinding.root)
        builder.setTitle(R.string.user_login)
        builder.setPositiveButton(R.string.tasker_setting_confirm) { dialogInterface: DialogInterface?, _: Int ->
            try {
                val username = ivBinding.etUsername.text.toString()
                val password = ivBinding.etPassword.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val token = repository.login(username, password)
                        if (!token.key.isNullOrEmpty()) {
                            MmkvManager.setToken(token.key)
                            _loginLiveData.postValue(true)
                        } else {
                            _loginLiveData.postValue(false)
                        }
                    } catch (e: Exception) {
                        _loginLiveData.postValue(false)
                        _errorLiveData.postValue(e.message)
                    }
                }
                dialogInterface?.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        builder.show()
    }

    fun checkLogin(notify: Boolean = true): Boolean {
        val token = MmkvManager.getToken()
        if (!token.isNullOrEmpty()) {
            if (notify) _loginLiveData.postValue(true)
            return true
        }
        if (notify) _loginLiveData.postValue(false)
        return false
    }
}