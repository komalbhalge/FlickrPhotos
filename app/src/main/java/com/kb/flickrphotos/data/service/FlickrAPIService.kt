package com.kb.flickrphotos.data.service

import com.kb.flickrphotos.data.model.PhotosResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FlickrAPIService {
    @GET("rest/")
    fun getSearchResults(
        @QueryMap params: Map<String, String>,
    ): Call<PhotosResponse>

}
