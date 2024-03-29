package com.tcode.pixabayclient.data

import com.tcode.pixabayclient.data.api.SearchResponse

interface ImagesDataSource {
    companion object {
        const val DEFAULT_IMAGES_PER_PAGE = 40
    }

    suspend fun searchImages(
        query: String,
        page: Int,
    ): SearchResponse

    suspend fun getImage(id: Long): SearchResponse
}
