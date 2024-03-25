package com.v2ray.ang.api.network

import android.content.Context
import android.content.Intent
import com.v2ray.ang.AngApplication
import com.v2ray.ang.dto.RefreshInput
import com.v2ray.ang.dto.RefreshResponse
import com.v2ray.ang.ui.LoginActivity
import com.v2ray.ang.util.MmkvManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
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
        if (request.header("withAuth") != null) {
            var token = MmkvManager.decodeAccessToken()
            var code = MmkvManager.getDeviceId()
            request = request.newBuilder()
                .removeHeader("withAuth")
                .addHeader(
                    "Authorization",
                    "Bearer $token"
                ).addHeader(
                    "Code",
                    "$code"
                )
                .build()
        }
        val lResponse: Response = chain.proceed(request)


        if (lResponse.code === 401) {
            if (request.url.encodedPath == "/auth/token/refresh/"){
                goToLogin()
            }
            val refreshToken: String? = MmkvManager.decodeRefreshToken()
            if (!refreshToken.isNullOrEmpty()) {
                val mApiServices = RestClient.getInstance().getApiService()
                val call: Call<RefreshResponse> =
                    mApiServices.refreshToken(RefreshInput(refreshToken))
                val tokenModelResponse: retrofit2.Response<RefreshResponse> = call.execute()
                if (tokenModelResponse.isSuccessful) {
                    MmkvManager.encodeAccessToken(tokenModelResponse.body()!!.access)
                    MmkvManager.encodeRefreshToken(tokenModelResponse.body()!!.refresh)
                    lResponse.request.newBuilder()
                        .removeHeader("Authorization")
                        .addHeader(
                            "Authorization",
                            "Bearer " + tokenModelResponse.body()?.access
                        )
                        .build()
                } else {
                    goToLogin()
                }
            } else {
                goToLogin()
            }
        }
        return lResponse
    }

    private fun goToLogin() {
        var intent = Intent(mContext, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext?.startActivity(intent)
    }
}
