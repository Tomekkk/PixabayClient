package com.tcode.pixabayclient.data

import com.tcode.pixabayclient.api.ApiKeyProvider
import com.tcode.pixabayclient.api.PixabayService
import com.tcode.pixabayclient.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImagesRemoteDataSource
    @Inject
    constructor(
        @IoDispatcher
        private val dispatcher: CoroutineDispatcher,
        private val pixabayService: PixabayService,
        private val apiKeyProvider: ApiKeyProvider,
    ) : ImagesDataSource {
        companion object {
            const val DEFAULT_PER_PAGE = 20
        }

        override suspend fun searchImages(
            query: String,
            page: Int,
        ): SearchResponse {
            return withContext(dispatcher) {
                pixabayService.searchImages(
                    apiKeyProvider.apiKey,
                    query,
                    page,
                    perPage = DEFAULT_PER_PAGE,
                )
            }
        }
    }
