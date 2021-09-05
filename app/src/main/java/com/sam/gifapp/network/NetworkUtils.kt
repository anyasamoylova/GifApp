package com.sam.gifapp.network

import android.content.Context
import android.net.ConnectivityManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File


class NetworkUtils(val context: Context) {
    val okHttpClient = provideOkHttpClient()
    val retrofitClient = provideRetrofitClient(okHttpClient)
    val apiService = provideApiService(retrofitClient)

    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val httpCacheDir = File(context.cacheDir, "response")
        val cacheSize = 8L * 1024 * 1024 //10Mb
        val cache = Cache(httpCacheDir, cacheSize)

        return OkHttpClient.Builder()
            .addInterceptor(AppInterceptor(context))
            .addInterceptor(loggingInterceptor)
            .cache(cache)
            .build()
    }

    @ExperimentalSerializationApi
    fun provideRetrofitClient(client: OkHttpClient): Retrofit {
        val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }
        val contentType = "application/json".toMediaType()

        //TODO move to BuildConfig
        val BASE_URL = "http://developerslife.ru/"

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
    }

    fun provideApiService(retrofit: Retrofit): DevLifeApiInterface {
        return retrofit.create(DevLifeApiInterface::class.java)
    }

    fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetwork != null
    }


}