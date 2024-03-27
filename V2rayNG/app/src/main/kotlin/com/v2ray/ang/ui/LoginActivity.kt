package com.v2ray.ang.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.v2ray.ang.R
import com.v2ray.ang.alertdialog.*
import com.v2ray.ang.databinding.ActivityLoginBinding
import com.v2ray.ang.extension.toast
import com.v2ray.ang.util.Utils.emailSupport
import com.v2ray.ang.viewmodel.UserViewModel
import kotlin.toString


class LoginActivity : AppCompatActivity(), MyAlertDialog.AlertDialogInterface {
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var alertDialogModel: MyAlertDialogViewModel

    private var username = ""
    private var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        binding.viewModel = userViewModel
        binding.lifecycleOwner = this
        alertDialogModel = AlertDialogManager.initializeViewModel(this);

        listener()
    }

    private fun listener() {
        binding.btLogin.setOnClickListener {
            username = binding.etUsername.text.toString()
            password = binding.etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                toast(R.string.login_error_filed, Toast.LENGTH_LONG)
            } else {
                userViewModel.login(username, password).observe(this) { login ->
                    if (login) {
                        var intent = Intent(this@LoginActivity, SplashScreenActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }


                userViewModel.mShowAPIError.observe(this) {
                    AlertDialogManager.showMyDialog(
                        this,
                        AlertType.Dynamic, alertDialogModel, this
                    );
                    alertDialogModel.setOptions(
                        AlertOptions(
                            title = getString(R.string.error_in_login),
                            text = it,
                            alternativeText = getString(R.string.call_support),
                            mainText = getString(R.string.close),
                            icon = R.drawable.icon_warning,
                            isCancelable = false,
                            type = AlertType.Unknown
                        )
                    );

                }
                userViewModel.mShowProgressBar.observe(this) { bt ->
                    if (bt) {
                        binding.progressBar.visibility = View.VISIBLE

                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                }
                userViewModel.mShowNetworkError.observe(this) {
                    AlertDialog.Builder(this).setMessage(R.string.app_no_internet_msg).show()
                }
            }
        }
    }

    override fun alertDialogMainOption(type: AlertType?) {
        this.finishAffinity();
    }

    override fun alertDialogAlternativeOption(type: AlertType?) {
        emailSupport()
    }
}