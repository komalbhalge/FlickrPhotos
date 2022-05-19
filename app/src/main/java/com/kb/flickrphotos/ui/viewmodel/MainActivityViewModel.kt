package com.kb.flickrphotos.ui.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kb.flickrphotos.controllers.Controller
import com.kb.flickrphotos.data.model.Photo
import com.kb.flickrphotos.domain.SearchFlickrPhotosUseCase
import com.kb.flickrphotos.utils.Resource
import com.kb.flickrphotos.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.security.AccessController.getContext
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val searchFlickrPhotosUseCase: SearchFlickrPhotosUseCase,
    val controller: Controller
) : ViewModel() {
    var searchedPhotosData: Resource<List<Photo>> by mutableStateOf(
        Resource(
            status = Status.LOADING,
            data = listOf(),
            message = ""
        )
    )

    fun getPhotosFromSearch(searchText: String) {
        // create a new coroutine on the ui thread
        viewModelScope.launch {

            searchFlickrPhotosUseCase.getSearchResults(searchText).let { photosList ->

                searchedPhotosData = photosList
            }
        }
    }
}