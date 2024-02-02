package com.tcode.pixabayclient.data

import com.tcode.pixabayclient.api.ApiKeyProvider
import com.tcode.pixabayclient.api.PixabayService
import com.tcode.pixabayclient.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteImagesDataSource
    @Inject
    constructor(
        @IoDispatcher
        private val dispatcher: CoroutineDispatcher,
        private val pixabayService: PixabayService,
        private val apiKeyProvider: ApiKeyProvider,
    ) : ImagesDataSource {
        override suspend fun searchImages(
            query: String,
            page: Int,
        ): SearchResponse {
            return withContext(dispatcher) {
                pixabayService.searchImages(
                    apiKeyProvider.apiKey,
                    query,
                    page,
                    perPage = ImagesDataSource.DEFAULT_IMAGES_PER_PAGE,
                )
            }
        }

        override suspend fun getImage(id: Long): SearchResponse {
            return withContext(dispatcher) {
                pixabayService.getImage(
                    apiKeyProvider.apiKey,
                    id,
                )
            }
        }
    }
