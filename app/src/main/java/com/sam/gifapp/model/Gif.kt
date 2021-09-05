package com.sam.gifapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Gif(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("description")
    val description: String = "",
    @SerialName("gifURL")
    val url: String = "",
)