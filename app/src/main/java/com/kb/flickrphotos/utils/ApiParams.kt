package com.kb.flickrphotos.utils

import com.kb.flickrphotos.BuildConfig.API_KEY

object ApiParams {

    var queryMap = mutableMapOf(
        Pair("method", "flickr.photos.search"),
        Pair("api_key", ""),
        Pair("format", "json"),
        Pair("nojsoncallback", "1"),
        Pair("safe_search", "1"),
        Pair("extras", "url_m, description"),
        Pair("per_page", "25")
    )

    fun setApiKey() {
        queryMap["api_key"] = API_KEY
    }

    fun getSearchQueryMap(searchText: String): Map<String, String> {
        queryMap["tags"] = searchText
        queryMap["page"] ="1"
        return queryMap
    }

}
