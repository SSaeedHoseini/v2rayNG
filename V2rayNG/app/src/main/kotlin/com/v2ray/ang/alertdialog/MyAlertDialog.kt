package com.v2ray.ang.alertdialog


import com.v2ray.ang.R
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider


class MyAlertDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //get shared ViewModel
        val viewModel: MyAlertDialogViewModel = ViewModelProvider(requireActivity()).get(
            MyAlertDialogViewModel::class.java
        )

        //get AlertOptions which decide how the shown dialog will look and behave
        val mOptions = if (viewModel.getOptions().getValue() != null) viewModel.getOptions()
            .getValue() else AlertOptions.create(AlertType.Unknown)

        //-----------------------------------LOAD UI------------------------------------------------
        val customView: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog_layout, null)
        val alertBuilder = AlertDialog.Builder(requireContext())
        alertBuilder.setView(customView)
        val dialog = alertBuilder.create()
        // This is needed to display my custom shape
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val iconIV = customView.findViewById<ImageView>(R.id.alert_dialog_icon)
        val circleFL = customView.findViewById<FrameLayout>(R.id.alert_dialog_circle)
        val titleTV = customView.findViewById<TextView>(R.id.alert_dialog_title)
        val textTV = customView.findViewById<TextView>(R.id.alert_dialog_text)
        val alternativeOptionTV =
            customView.findViewById<TextView>(R.id.alert_dialog_alternative_option)
        val mainOptionTV = customView.findViewById<TextView>(R.id.alert_dialog_main_option)
        iconIV.setImageDrawable(ContextCompat.getDrawable(requireContext(), mOptions!!.icon))
        titleTV.text = mOptions?.title
        alternativeOptionTV.text = mOptions?.alternativeText
        mainOptionTV.text = mOptions?.mainText
        isCancelable = mOptions!!.isCancelable

        //if mainText field of AlertOptions is empty then hide it
        if (mOptions?.mainText!!.isEmpty()) {
            mainOptionTV.visibility = View.GONE
        } else {
            mainOptionTV.setOnClickListener { v: View? ->
                mCallback!!.alertDialogMainOption(mOptions?.getType())
                dismiss()
            }
        }

        //if alternativeText field of AlertOptions is empty then hide it
        if (mOptions.alternativeText.isEmpty()) {
            alternativeOptionTV.visibility = View.GONE
        } else {
            alternativeOptionTV.setOnClickListener { v: View? ->
                mCallback!!.alertDialogAlternativeOption(mOptions.getType())
                dismiss()
            }
        }

        //change colors depending on AlertCategory
        val alertCategory = mOptions?.let { getAlertCategory(it.getType()) }
        if (alertCategory === AlertCategory.Warning) {
            circleFL.setBackgroundResource(R.drawable.alert_dialog_circle_warning)
            alternativeOptionTV.setTextColor(getColorHelper(requireContext(), R.color.warning))
            mainOptionTV.setBackgroundResource(R.drawable.background_rounded_button_warning)
        } else if (alertCategory === AlertCategory.Success) {
            circleFL.setBackgroundResource(R.drawable.alert_dialog_circle_success)
            alternativeOptionTV.setTextColor(getColorHelper(requireContext(), R.color.correct))
            mainOptionTV.setBackgroundResource(R.drawable.background_rounded_button_success)
        }

        //------------------------Observe changes in ViewModel---------------------------------------

        // I only alter the text in my dynamic alert dialog but here you could theoretically alter
        // everything dynamically
        viewModel.getOptions().observe(this) { options -> textTV.text = options.text }

        // I use the shared ViewModel to observe if from somewhere else cancel() has been called
        // If MyAlertDialogViewModel.cancel.getValue() == true, then dismiss dialog
        viewModel.isCanceled.observe(this) { cancel ->
            if (cancel) {
                dismiss()
            }
        }
        return dialog
    }

    /**
     * @param type takes AlertType which is unique for every alert dialog of same purpose
     * @return AlertCategory (currently: primary, warning or success)
     */
    private fun getAlertCategory(type: AlertType): AlertCategory {
        //category success
        if (type === AlertType.Success) {
            return AlertCategory.Success
        }
        //category primary
        return if (type === AlertType.Dynamic || type === AlertType.Primary) {
            AlertCategory.Primary
        } else {
            //category warning
            AlertCategory.Warning
        }
    }

    interface AlertDialogInterface {
        fun alertDialogMainOption(type: AlertType?)
        fun alertDialogAlternativeOption(type: AlertType?)
    }

    companion object {
        private var mCallback: AlertDialogInterface? = null
        fun newInstance(callback: AlertDialogInterface?): MyAlertDialog {
            mCallback = callback
            return MyAlertDialog()
        }
    }
}