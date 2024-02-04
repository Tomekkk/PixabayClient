package com.tcode.pixabayclient.domain

import com.tcode.pixabayclient.data.ImagesRepository
import javax.inject.Inject

class GetImageDetailsUseCase
    @Inject
    constructor(
        private val imagesRepository: ImagesRepository,
    ) {
        suspend fun get(id: Long): ImageDetails? {
            return imagesRepository.getImage(id)?.let { imageEntity ->
                return ImageDetails(
                    id = imageEntity.imageId,
                    tags = imageEntity.tags,
                    user = imageEntity.user,
                    largeImageURL = imageEntity.largeImageURL,
                    downloads = imageEntity.downloads,
                    likes = imageEntity.likes,
                    comments = imageEntity.comments,
                    aspectRatio = imageEntity.previewWidth / imageEntity.previewHeight.toFloat(),
                )
            }
        }
    }
