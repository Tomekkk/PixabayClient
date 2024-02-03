package com.tcode.pixabayclient.data

import com.tcode.pixabayclient.data.api.ImageDto

object ImagesFactory {
    fun createDto(range: IntRange): List<ImageDto> =
        range.map {
            ImageDto(
                id = it.toLong(),
                tags = "tags",
                previewURL = "previewURL$it",
                previewWidth = 1,
                previewHeight = 1,
                largeImageURL = "largeImageURL$it",
                imageWidth = 1,
                imageHeight = 1,
                downloads = it.toLong(),
                likes = it.toLong(),
                comments = it.toLong(),
                user = "user$it",
            )
        }
}
