package com.v2ray.ang

import android.content.Context
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import androidx.work.Configuration
import com.tencent.mmkv.MMKV
import com.v2ray.ang.util.MmkvManager
import com.v2ray.ang.util.Utils


class AngApplication : MultiDexApplication(), Configuration.Provider {
    private val settingsStorage by lazy { MMKV.mmkvWithID(MmkvManager.ID_SETTING, MMKV.MULTI_PROCESS_MODE) }

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
        if (firstRun)
            defaultSharedPreferences.edit().putInt(PREF_LAST_VERSION, BuildConfig.VERSION_CODE).apply()

        //Logger.init().logLevel(if (BuildConfig.DEBUG) LogLevel.FULL else LogLevel.NONE)
        MMKV.initialize(this)
        loadDataToPreference(
            AppConfig.PREF_V2RAY_ROUTING_AGENT,
            AppConfig.TAG_AGENT
        )
        loadDataToPreference(
            AppConfig.PREF_V2RAY_ROUTING_DIRECT,
            AppConfig.TAG_DIRECT
        )
        loadDataToPreference(
            AppConfig.PREF_V2RAY_ROUTING_BLOCKED,
            AppConfig.TAG_BLOCKED
        )
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setDefaultProcessName("${BuildConfig.APPLICATION_ID}:bg")
            .build()
    }
    private fun loadDataToPreference(
        pref: String,
        tag: String
    ) {
        val content = Utils.readTextFromAssets(this, "custom_routing_$tag")
        settingsStorage.putString(pref,content).apply()
    }
}