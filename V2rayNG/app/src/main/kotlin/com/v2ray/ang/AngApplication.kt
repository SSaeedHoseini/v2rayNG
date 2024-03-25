package com.v2ray.ang

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import androidx.work.Configuration
import com.google.android.gms.tasks.OnCompleteListener
import com.tencent.mmkv.MMKV
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging

class AngApplication : MultiDexApplication(), Configuration.Provider {
    companion object {
        const val PREF_LAST_VERSION = "pref_last_version"
        lateinit var application: AngApplication
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        application = this
    }

    var firstRun = false
        private set

    override fun onCreate() {
        super.onCreate()

//        LeakCanary.install(this)

        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        firstRun = defaultSharedPreferences.getInt(PREF_LAST_VERSION, 0) != BuildConfig.VERSION_CODE
        if (firstRun) {
            //TODO update this code - change to other location if needed
            Firebase.messaging.token.addOnCompleteListener(
                 OnCompleteListener{ task ->
                     if (!task.isSuccessful) {
                         Log.w("Firebase", "Fetching FCM registration token failed", task.exception)
                         return@OnCompleteListener
                     }
                     // Get new FCM registration token
                     val token = task.result.toString()

                     // Log and toast
                     Log.i("Firebase", token)
                }
            )
            defaultSharedPreferences.edit().putInt(PREF_LAST_VERSION, BuildConfig.VERSION_CODE)
                .apply()
        }
        //Logger.init().logLevel(if (BuildConfig.DEBUG) LogLevel.FULL else LogLevel.NONE)
        MMKV.initialize(this)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setDefaultProcessName("${BuildConfig.APPLICATION_ID}:bg")
            .build()
    }
}
