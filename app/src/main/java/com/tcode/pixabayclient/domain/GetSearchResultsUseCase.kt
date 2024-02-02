package com.tcode.pixabayclient.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.tcode.pixabayclient.data.ImagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSearchResultsUseCase
    @Inject
    constructor(private val imagesRepository: ImagesRepository) {
        fun getResults(query: String): Flow<PagingData<ImageResult>> =
            imagesRepository.getImagesStream(query).map { pagingData ->
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
