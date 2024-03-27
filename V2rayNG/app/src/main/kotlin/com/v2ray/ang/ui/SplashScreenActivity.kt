package com.v2ray.ang.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.v2ray.ang.AngApplication
import com.v2ray.ang.BuildConfig
import com.v2ray.ang.R
import com.v2ray.ang.databinding.ActivitySplashScreenBinding
import com.v2ray.ang.util.MmkvManager
import com.v2ray.ang.util.SpeedtestUtil
import com.v2ray.ang.viewmodel.MainViewModel

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        val mainViewModel: MainViewModel by viewModels()
        mainViewModel.startListenBroadcast()

        setContentView(view)
        binding.tvVersion.text = "v${BuildConfig.VERSION_NAME} (${SpeedtestUtil.getLibVersion()})"
        binding.ivV2rayngplus.alpha = 0F
        binding.ivV2rayngplus.animate().setDuration(5000).alpha(1F).withEndAction {
            val token = MmkvManager.decodeToken()
            if (!token.isNullOrEmpty()){
                val i = Intent(AngApplication.application, MainActivity::class.java)
                startActivity(i)
                finish()
            } else{
                val i2 = Intent(AngApplication.application, LoginActivity::class.java)
                startActivity(i2)
                finish()
            }
        }
    }
}
