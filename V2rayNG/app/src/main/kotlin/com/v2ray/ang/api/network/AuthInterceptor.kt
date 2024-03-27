package com.v2ray.ang.api.network

import android.content.Context
import android.content.Intent
import com.v2ray.ang.AngApplication
import com.v2ray.ang.ui.LoginActivity
import com.v2ray.ang.util.MmkvManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthInterceptor : Interceptor {
        private var mApplication: AngApplication
        private var mContext: Context?

        constructor(context: Context?) {
            mApplication = AngApplication.application!!
            mContext = context
        }

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request: Request = chain.request()
            var token = MmkvManager.decodeToken()

            if (!token.isNullOrEmpty()) {
                request = request.newBuilder()
                    .addHeader("Authorization", "Token $token")
                    .build()
            }

            val lResponse: Response = chain.proceed(request)

            if (lResponse.code === 401) {
                goToLogin()
            }
            return lResponse
        }

        private fun goToLogin() {
            var intent = Intent(mContext, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext?.startActivity(intent)
        }
    }

