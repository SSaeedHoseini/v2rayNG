package com.v2ray.ang.alertdialog

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner


object AlertDialogManager {
    /**
     * @param owner if out of Activity usually it is enough to just call 'this' or 'ActivityName.this'.
     * if initialized in a fragment always call requireActivity() instead
     * @return MyAlertDialogViewModel which holds alert dialogs options
     */
    fun initializeViewModel(owner: ViewModelStoreOwner?): MyAlertDialogViewModel {
        return ViewModelProvider(owner!!)[MyAlertDialogViewModel::class.java]
    }

    /**
     * @param callback  usually this, or ActivityName.this. Has to be class which extends any FragmentActivity
     * @param type      the AlertType which defines what kind of alert to show
     * @param viewModel the MyAlertDialogViewModel instance of the activity of where this function is called
     * @param context   If called inside of a fragment, put requireActivity()
     */
    fun showMyDialog(
        callback: MyAlertDialog.AlertDialogInterface?,
        type: AlertType,
        viewModel: MyAlertDialogViewModel,
        context: Context
    ) {
        val dialog: MyAlertDialog = MyAlertDialog.newInstance(callback)
        viewModel.setOptions(AlertOptions.create(type))
        viewModel.showDialog() //important otherwise won't show
        dialog.show((context as FragmentActivity).supportFragmentManager, type.toString())
    }
}