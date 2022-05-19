package com.kb.flickrphotos.data.model

import com.example.example.Description
import com.google.gson.annotations.SerializedName


data class Photo(
    @SerializedName("id") val id: String?,
    @SerializedName("owner") val owner: String?,
    @SerializedName("secret") val secret: String?,
    @SerializedName("server") val server: String?,
    @SerializedName("farm") val farm: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: Description?,
    @SerializedName("ispublic") val isPublic: Int?,
    @SerializedName("isfriend") val isFriend: Int?,
    @SerializedName("isfamily") val isFamily: Int?,
    @SerializedName("url_m") var photo_url: String? = "",
    @SerializedName("height_m") val height: Int? = 0,
    @SerializedName("width_m") val width: Int? = 0,
)
