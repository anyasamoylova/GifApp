package com.sam.gifapp.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DevLifeApiInterface {
    @GET("latest/{page}")
    fun callApiForLatestGifs(@Path("page") page: Int): Call<DevLifeResponse>

    @GET("top/{page}")
    fun callApiForTopGifs(@Path("page") page: Int): Call<DevLifeResponse>

    @GET("hot/{page}")
    fun callApiForHotGifs(@Path("page") page: Int): Call<DevLifeResponse>
}