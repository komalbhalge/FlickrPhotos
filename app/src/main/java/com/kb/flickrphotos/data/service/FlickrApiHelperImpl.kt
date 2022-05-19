package com.kb.flickrphotos.data.service

import com.kb.flickrphotos.data.model.PhotosResponse
import retrofit2.Call
import javax.inject.Inject

class FlickrApiHelperImpl @Inject constructor(private val apiService: FlickrAPIService) :
    FlickrApiHelper {
    override fun getSearchPictures(params: Map<String, String>): Call<PhotosResponse> =
        apiService.getSearchResults(params)
}