package com.tcode.pixabayclient.data

import com.tcode.pixabayclient.data.api.ApiKeyProvider
import com.tcode.pixabayclient.data.api.PixabayService
import com.tcode.pixabayclient.data.api.SearchResponse
import javax.inject.Inject

class RemoteImagesDataSource
    @Inject
    constructor(
        private val pixabayService: PixabayService,
        private val apiKeyProvider: ApiKeyProvider,
    ) : ImagesDataSource {
        override suspend fun searchImages(
            query: String,
            page: Int,
        ): SearchResponse =
            pixabayService.searchImages(
                apiKeyProvider.apiKey,
                query,
                page,
                perPage = ImagesDataSource.DEFAULT_IMAGES_PER_PAGE,
            )

        override suspend fun getImage(id: Long): SearchResponse =
            pixabayService.getImage(
                apiKeyProvider.apiKey,
                id,
            )
    }
