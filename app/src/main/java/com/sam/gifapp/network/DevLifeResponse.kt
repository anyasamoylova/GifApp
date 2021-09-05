package com.sam.gifapp.network

import com.sam.gifapp.model.Gif
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DevLifeResponse (
    @SerialName("result")
    val result: List<Gif> = listOf()
    )