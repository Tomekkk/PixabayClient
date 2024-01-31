package com.tcode.pixabayclient.data

object ImagesFactory {
    fun create(range: LongRange): List<ImageDto> =
        range.map {
            ImageDto(
                id = it,
                tags = "tags",
                previewURL = "previewURL",
                largeImageURL = "largeImageURL",
                downloads = it,
                likes = it,
                comments = it,
                userId = it,
                user = "user$it",
            )
        }
}
