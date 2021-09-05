package com.sam.gifapp.network

import com.sam.gifapp.model.Gif

interface ResponseCallback {
    fun onSuccess(gifs: List<Gif>)
    fun onFailure()
}