package gc.v2ray.angplus.api.network

import android.content.Context
import com.v2ray.ang.util.Utils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class I18nInterceptor(context: Context?) : Interceptor {
    private var mContext: Context? = context

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val lang = Utils.getLocale(mContext).language

        request = request.newBuilder()
            .addHeader(
                "Accept-Language",
                lang
            )
            .build()
        return chain.proceed(request)
    }
}
