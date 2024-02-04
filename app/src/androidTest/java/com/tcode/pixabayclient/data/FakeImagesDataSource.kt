package com.tcode.pixabayclient.data

import com.tcode.pixabayclient.data.api.ImageDto
import com.tcode.pixabayclient.data.api.SearchResponse

class FakeImagesDataSource : ImagesDataSource {
    var exception: Exception? = null
    private val images = mutableListOf<ImageDto>()

    fun setImages(images: List<ImageDto>) {
        this.images.addAll(images)
    }

    override suspend fun searchImages(
        query: String,
        page: Int,
    ): SearchResponse {
        exception?.let { throw it }
        return images.toList().toResponse()
    }

    override suspend fun getImage(id: Long): SearchResponse {
        throw NotImplementedError()
    }

    private fun List<ImageDto>.toResponse() = SearchResponse(this, 1, 1)

    fun tearDown() {
        images.clear()
        exception = null
    }
}
