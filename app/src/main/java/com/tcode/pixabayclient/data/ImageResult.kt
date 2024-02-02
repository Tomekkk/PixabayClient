package com.tcode.pixabayclient.data

data class ImageResult(
    val uniqueId: UniqueId,
    val id: Long,
    val tags: String,
    val previewURL: String,
    val aspectRatio: Float,
    val user: String,
)
