package com.tcode.pixabayclient.data

object ImagesFactory {
    fun createDto(range: LongRange): List<ImageDto> =
        range.map {
            ImageDto(
                id = it,
                tags = "tags",
                previewURL = "previewURL",
                previewWidth = 1,
                previewHeight = 1,
                largeImageURL = "largeImageURL",
                imageWidth = 1,
                imageHeight = 1,
                downloads = it,
                likes = it,
                comments = it,
                user = "user$it",
            )
        }

    fun create(
        range: LongRange,
        uniqueIdProvider: UniqueIdProvider,
    ): List<ImageResult> =
        range.map {
            ImageResult(
                uniqueId = uniqueIdProvider.provideUniqueId(),
                id = it,
                tags = "tags",
                previewURL = "previewURL",
                aspectRatio = 1F,
                user = "user$it",
            )
        }
}
