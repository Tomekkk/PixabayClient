package com.tcode.pixabayclient.data

interface ImagesDataSource {
    suspend fun searchImages(
        query: String,
        page: Int,
    ): SearchResponse
}
