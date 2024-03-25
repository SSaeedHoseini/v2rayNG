package com.v2ray.ang.alertdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MyAlertDialogViewModel : ViewModel() {
    private val options = MutableLiveData<AlertOptions>()
    private val cancel = MutableLiveData<Boolean>()
    fun getOptions(): LiveData<AlertOptions> {
        return options
    }

    fun setOptions(newOptions: AlertOptions) {
        options.value = newOptions
    }

    fun showDialog() {
        cancel.value = false
    }

    fun cancelAlert() {
        cancel.value = true
    }

    val isCanceled: LiveData<Boolean>
        get() = cancel
}