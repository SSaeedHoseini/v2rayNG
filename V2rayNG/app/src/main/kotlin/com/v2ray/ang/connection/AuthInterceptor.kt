package gc.v2ray.angplus.api.network

import android.content.Context
import android.content.Intent
import com.v2ray.ang.AngApplication
import com.v2ray.ang.util.MmkvManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import java.io.IOException

class AuthInterceptor : Interceptor {
    private var mContext: Context?

    constructor(context: Context?) {
        mContext = context
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        if (request.url.encodedPath != "/api/user/login/") {
            val token = MmkvManager.getToken()
            val code = MmkvManager.getDeviceId()
            request = request.newBuilder()
                .removeHeader("withAuth")
                .addHeader(
                    "Authorization",
                    "Token $token"
                ).addHeader(
                    "Code",
                    "$code"
                )
                .build()
        }
        return chain.proceed(request)
    }
}
