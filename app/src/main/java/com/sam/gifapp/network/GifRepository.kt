package com.sam.gifapp.network

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sam.gifapp.model.Gif
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GifRepository (context: Context) {
    companion object {
        private const val TAG = "GifRepository"
    }

    private var apiInterface = NetworkUtils(context).apiService

    fun getLatestGifs(page: Int, callback: ResponseCallback) {
        val call: Call<DevLifeResponse> = apiInterface.callApiForLatestGifs(page)
        enqueueCall(call, callback)
    }

    fun getTopGifs(page: Int, callback: ResponseCallback) {
        val call: Call<DevLifeResponse> = apiInterface.callApiForTopGifs(page)
        enqueueCall(call, callback)
    }

    fun getHotGifs(page: Int, callback: ResponseCallback) {
        val call: Call<DevLifeResponse> = apiInterface.callApiForHotGifs(page)
        enqueueCall(call, callback)
    }

    fun enqueueCall(call: Call<DevLifeResponse>, callback: ResponseCallback) {
        call.enqueue(object: Callback<DevLifeResponse>{
            override fun onResponse(
                call: Call<DevLifeResponse>,
                response: Response<DevLifeResponse>,
            ) {
                if (response.body() != null) {
                    callback.onSuccess(response.body()!!.result)
                } else {
                    Log.e(TAG, "Response body is empty")
                    callback.onFailure()
                }
            }

            override fun onFailure(call: Call<DevLifeResponse>, t: Throwable) {
                Log.e(TAG, t.localizedMessage)
                callback.onFailure()
            }

        })
    }
}