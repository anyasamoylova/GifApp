package com.sam.gifapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sam.gifapp.model.Gif
import com.sam.gifapp.network.GifRepository
import com.sam.gifapp.network.ResponseCallback
import com.sam.gifapp.ui.GifFragment
import okhttp3.internal.notify

class GifViewModel : ViewModel() {

    lateinit var repo: GifRepository

    private var pageLatest = 0
    private var pageTop = 0
    private var pageHot = 0

    var positionLatest = 0
    var positionTop = 0
    var positionHot = 0

    var latestGifs = MutableLiveData<List<Gif>>(listOf())
    var topGifs = MutableLiveData<List<Gif>>(listOf())
    var hotGifs = MutableLiveData<List<Gif>>(listOf())

    var dataLatestState = MutableLiveData(DataState.DOWNLOAD)
    var dataTopState = MutableLiveData(DataState.DOWNLOAD)
    var dataHotState = MutableLiveData(DataState.DOWNLOAD)

    var tabPosition = -1

    fun updateLatestGif() {
        dataLatestState.value = DataState.DOWNLOAD
        repo.getLatestGifs(pageLatest, object: ResponseCallback {
            override fun onSuccess(gifs: List<Gif>) {
                latestGifs.value = latestGifs.value?.plus(gifs)
                if (gifs.isNotEmpty()) {
                    dataLatestState.value = DataState.SUCCESS
                } else dataLatestState.value = DataState.EMPTY
            }

            override fun onFailure() {
                dataLatestState.value = DataState.ERROR
            }
        })
        pageLatest++
    }

    fun updateTopGif() {
        dataTopState.value = DataState.DOWNLOAD
        repo.getTopGifs(pageTop, object: ResponseCallback {
            override fun onSuccess(gifs: List<Gif>) {
                topGifs.value = topGifs.value?.plus(gifs)
                if (gifs.isNotEmpty()) {
                    dataTopState.value = DataState.SUCCESS
                } else dataTopState.value = DataState.EMPTY
            }

            override fun onFailure() {
                dataTopState.value = DataState.ERROR
            }

        })
        pageTop++
    }

    fun updateHotGif() {
        dataHotState.value = DataState.DOWNLOAD
        repo.getHotGifs(pageHot, object: ResponseCallback {
            override fun onSuccess(gifs: List<Gif>) {
                hotGifs.value = hotGifs.value?.plus(gifs)
                if (gifs.isNotEmpty()) {
                    dataHotState.value = DataState.SUCCESS
                } else dataHotState.value = DataState.EMPTY
            }

            override fun onFailure() {
                dataHotState.value = DataState.ERROR
            }

        })
        pageHot++
    }

    fun increasePosition(fragmentType: Int) {
        when (fragmentType) {
            GifFragment.TYPE_LATEST -> positionLatest++
            GifFragment.TYPE_TOP -> positionTop++
            GifFragment.TYPE_HOT -> positionHot++
        }
    }

    fun decreasePosition(fragmentType: Int) {
        when (fragmentType) {
            GifFragment.TYPE_LATEST -> positionLatest--
            GifFragment.TYPE_TOP -> positionTop--
            GifFragment.TYPE_HOT -> positionHot--
        }
    }



}