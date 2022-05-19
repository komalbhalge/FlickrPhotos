package com.kb.flickrphotos.data.repositories

import com.kb.flickrphotos.data.model.Photo
import com.kb.flickrphotos.data.service.FlickrApiHelper
import com.kb.flickrphotos.utils.ApiParams
import com.kb.flickrphotos.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class APIRepository @Inject constructor(private val flickrApiHelper: FlickrApiHelper) {

    suspend fun searchPicture(searchTag: String): Resource<List<Photo>> {
        return withContext(Dispatchers.IO) {
            try {
                val searchRequest = ApiParams.getSearchQueryMap(searchText = searchTag)
                val pictures = flickrApiHelper.getSearchPictures(searchRequest).execute()
                val data = pictures.body()?.photos?.photos
                Resource.success(data = data)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.error(e.localizedMessage ?: "Exception occurred!", data = emptyList())
            }
        }
    }
}