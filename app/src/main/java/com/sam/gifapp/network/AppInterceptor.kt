package com.sam.gifapp.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AppInterceptor(val context: Context) : Interceptor {

    //adds default parameters to the request
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter("json", "true")
            .build()

        val request = chain.request().newBuilder()
            .url(url)
            .build()

        val response = chain.proceed(request)

        //read from cache for 5 minutes
        return if (NetworkUtils(context).isNetworkConnected()) {
            val maxAge = 300
            response.newBuilder()
                .header("Cache-Control", "public, max-age=${maxAge}")
                .build()
        } else { //if internet isn't available, read from cache for a day
            val maxAge = 60 * 60 * 24
            response.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=${maxAge}")
                .build()
        }


    }
}