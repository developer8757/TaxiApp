package com.rontaxi.retrofit

import android.content.Context
import com.rontaxi.cache.getToken
import okhttp3.Interceptor
import okhttp3.Response

class TaxiInterceptor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        val headers = request.headers().newBuilder()
            .add("Content-Type", "application/json")
            .add("Accept", "application/json")
            .add(
                "Authorization",
                "Bearer ${getToken(context)}"
            )
            .build()

        request = request.newBuilder().headers(headers).build()

        return chain.proceed(request)
    }
}