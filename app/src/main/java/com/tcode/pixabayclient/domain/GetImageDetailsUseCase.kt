package com.tcode.pixabayclient.domain

import com.tcode.pixabayclient.data.ImageDetails
import com.tcode.pixabayclient.data.ImagesRepository
import javax.inject.Inject

class GetImageDetailsUseCase
    @Inject
    constructor(
        private val imagesRepository: ImagesRepository,
    ) {
        suspend fun get(id: Long): ImageDetails? = imagesRepository.getImage(id)
    }
