package com.tcode.pixabayclient.domain

data class ImageResult(
    val id: Long,
    val imageId: Long,
    val tags: String,
    val previewURL: String,
    val aspectRatio: Float,
    val user: String,
)
