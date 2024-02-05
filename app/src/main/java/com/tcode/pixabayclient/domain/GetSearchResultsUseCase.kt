package com.tcode.pixabayclient.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.tcode.pixabayclient.data.ImagesDataSource
import com.tcode.pixabayclient.data.ImagesRepository
import com.tcode.pixabayclient.data.mediator.ImagesRemoteMediatorFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSearchResultsUseCase
    @Inject
    constructor(
        private val imagesRepository: ImagesRepository,
        private val imagesRemoteMediatorFactory: ImagesRemoteMediatorFactory,
    ) {
        @OptIn(ExperimentalPagingApi::class)
        operator fun invoke(query: String): Flow<PagingData<ImageResult>> {
            val unifiedQuery = query.trim().lowercase()
            return Pager(
                config =
                    PagingConfig(
                        pageSize = ImagesDataSource.DEFAULT_IMAGES_PER_PAGE,
                        maxSize = ImagesDataSource.DEFAULT_IMAGES_PER_PAGE * 3,
                        enablePlaceholders = false,
                    ),
                remoteMediator = imagesRemoteMediatorFactory.create(unifiedQuery),
            ) {
                imagesRepository.getPagingSource(unifiedQuery)
            }.flow.map { pagingData ->
                pagingData.map { imageEntity ->
                    ImageResult(
                        id = imageEntity.id!!,
                        imageId = imageEntity.imageId,
                        tags = imageEntity.tags,
                        user = imageEntity.user,
                        previewURL = imageEntity.previewURL,
                        aspectRatio = imageEntity.previewWidth / imageEntity.previewHeight.toFloat(),
                    )
                }
            }
        }
    }
