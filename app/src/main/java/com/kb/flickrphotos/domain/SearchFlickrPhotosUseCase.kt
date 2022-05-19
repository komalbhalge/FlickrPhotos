package com.kb.flickrphotos.domain

import com.kb.flickrphotos.data.model.Photo
import com.kb.flickrphotos.data.repositories.APIRepository
import com.kb.flickrphotos.utils.Resource
import javax.inject.Inject

class SearchFlickrPhotosUseCase @Inject constructor(
    private val apiRepository: APIRepository
) {
    suspend fun getSearchResults(searchTag: String): Resource<List<Photo>> {
        return apiRepository.searchPicture(searchTag)
    }
}